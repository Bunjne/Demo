package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_assignment.student

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.data.entity.AssignmentItemDTO
import whiz.sspark.library.data.static.PagingConfiguration
import whiz.sspark.library.data.viewModel.ClassAssignmentViewModel
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.assignment.AssignmentAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentStudentClassAssignmentBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.presentation.assignment.AssignmentDetailActivity

open class StudentClassAssignmentFragment : BaseFragment() {

    companion object {
        fun newInstance(classGroupId: String, startColor: Int) = StudentClassAssignmentFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
                putInt("startColor", startColor)
            }
        }
    }

    protected val viewModel: ClassAssignmentViewModel by viewModel()

    protected val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    private val startColor by lazy {
        arguments?.getInt("startColor") ?: ContextCompat.getColor(requireContext(), R.color.primaryStartColor)
    }

    private var _binding: FragmentStudentClassAssignmentBinding? = null
    protected val binding get() = _binding!!

    private var currentPage = PagingConfiguration.INITIAL_PAGE
    private var totalPage = PagingConfiguration.INITIAL_TOTAL_PAGE
    private var assignments = mutableListOf<AssignmentItemDTO>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStudentClassAssignmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            assignments = savedInstanceState.getString("assignments")?.toObjects(Array<AssignmentItemDTO>::class.java) ?: mutableListOf()
            currentPage = savedInstanceState.getInt("currentPage")
            totalPage = savedInstanceState.getInt("totalPage")
            initView()
            updateAdapterItem()
        } else {
            initView()
            viewModel.getLatestAssignments(classGroupId, PagingConfiguration.INITIAL_PAGE, PagingConfiguration.PAGE_SIZE)
        }
    }

    override fun initView() {
        binding.vAssignment.init(
            progressbarColor = startColor,
            onAssignmentClicked = { assignment ->
                onNavigateToAssignmentDetail(assignment)
            },
            onRefresh = {
                viewModel.getLatestAssignments(classGroupId, PagingConfiguration.INITIAL_PAGE, PagingConfiguration.PAGE_SIZE)
            },
            onLoadMore = {
                val nextPage = currentPage + PagingConfiguration.PAGE_INCREASE_STEP
                viewModel.getAssignments(classGroupId, nextPage, PagingConfiguration.PAGE_SIZE)
            }
        )
    }

    protected open fun onNavigateToAssignmentDetail(assignment: Assignment) {
        val intent = Intent(requireContext(), AssignmentDetailActivity::class.java).apply {
            putExtra("assignment", assignment.toJson())
        }

        startActivity(intent)
    }

    override fun observeView() {
        viewModel.latestAssignmentLoading.observe(this) { isLoading ->
            binding.vAssignment.setSwipeRefreshLayout(isLoading)
        }

        viewModel.oldAssignmentLoading.observe(this) { isLoading ->
            binding.vAssignment.setIsLoading(isLoading)
        }
    }

    override fun observeData() {
        viewModel.latestAssignmentResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                binding.vAssignment.clearOldItem {
                    currentPage = PagingConfiguration.INITIAL_PAGE
                    totalPage = it.totalPage

                    with(assignments) {
                        clear()
                        addAll(it.items)
                    }

                    checkIsLastPage()
                    updateAdapterItem()
                }
            }
        }

        viewModel.assignmentResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                currentPage += PagingConfiguration.PAGE_INCREASE_STEP

                assignments.addAll(it.items)

                checkIsLastPage()
                updateAdapterItem()
            }
        }
    }

    override fun observeError() {
        viewModel.assignmentErrorResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                showApiResponseXAlert(requireContext(), it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.getContentIfNotHandled()?.let {
                requireContext().showAlertWithOkButton(it)
            }
        }
    }

    private fun updateAdapterItem() {
        val items = mutableListOf<AssignmentAdapter.AssignmentItem>()
        val isLastPage = currentPage >= totalPage

        val convertedAssignments = convertToAdapterItem()
        items.addAll(convertedAssignments)

        if (!isLastPage) {
            items.add(AssignmentAdapter.AssignmentItem.Loading(false))
        }

        binding.vAssignment.updateItem(items)
    }

    private fun convertToAdapterItem(): List<AssignmentAdapter.AssignmentItem.Item> {
        return assignments.map { AssignmentAdapter.AssignmentItem.Item(
            Assignment(
                id = it.id,
                startColor = it.classGroup.colorCode1,
                endColor = it.classGroup.colorCode2,
                courseTitle = resources.getString(R.string.assignment_student_title, it.classGroup.code, it.classGroup.name),
                classGroupId = it.classGroup.classGroupId,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                deadlineAt = it.deadlineAt,
                title = it.title,
                description = it.message,
                instructorName = it.instructor.fullName,
                instructorId = it.instructor.id,
                imageUrl = it.instructor.imageUrl,
                gender = it.instructor.gender,
                attachments = it.attachments
            )
        ) }
    }

    private fun checkIsLastPage() {
        val isLastPage = currentPage >= totalPage
        binding.vAssignment.setIsLastPage(isLastPage)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("assignments", assignments.toJson())
        outState.putInt("currentPage", currentPage)
        outState.putInt("totalPage", totalPage)
    }
}