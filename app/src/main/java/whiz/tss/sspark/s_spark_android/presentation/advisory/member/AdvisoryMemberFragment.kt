package whiz.tss.sspark.s_spark_android.presentation.advisory.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.viewModel.ClassMemberViewModel
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.advisory.member.AdvisoryMemberAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.databinding.FragmentAdvisoryMemberBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.utility.retrieveUserID

class AdvisoryMemberFragment : BaseFragment() {

    companion object {
        fun newInstance(classGroupId: String) = AdvisoryMemberFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
            }
        }
    }

    private var _binding: FragmentAdvisoryMemberBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClassMemberViewModel by viewModel()

    private val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    private val userId by lazy {
        retrieveUserID(requireContext())
    }

    val items = mutableListOf<AdvisoryMemberAdapter.Item>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAdvisoryMemberBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            initView()

            viewModel.getClassMember(classGroupId, false)
        }
    }

    override fun initView() {
        binding.vAdvisoryMember.init(
            items = items,
            onRefresh = {
                viewModel.getClassMember(classGroupId, true)
            },
            onChatMemberClicked = {

            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this, Observer { isLoading ->
            binding.vAdvisoryMember.setSwipeRefreshLayout(isLoading)
        })
    }

    override fun observeData() {
        viewModel.memberResponse.observe(this, Observer {
            it?.let { member ->
                val instructorCounts = member.instructors.size
                val studentCounts = member.students.size

                val newItems = mutableListOf<AdvisoryMemberAdapter.Item>()
                with(newItems) {
                    if (member.instructors.isNotEmpty()) {
                        val instructorTitle = AdvisoryMemberAdapter.Item(
                            title = resources.getString(R.string.advisory_member_instructor_title, instructorCounts)
                        )

                        add(instructorTitle)

                        val isChatEnable = SSparkApp.role == RoleType.STUDENT_SENIOR
                        val instructors = member.instructors.map { instructor ->
                            AdvisoryMemberAdapter.Item(
                                instructor = instructor,
                                isChatEnable = isChatEnable
                            )
                        }

                        addAll(instructors)
                    }

                    if (member.students.isNotEmpty()) {
                        val studentTitle = AdvisoryMemberAdapter.Item(
                            title = resources.getString(R.string.class_member_student_title, studentCounts)
                        )

                        add(studentTitle)

                        val isChatEnable = SSparkApp.role == RoleType.INSTRUCTOR_SENIOR
                        val students = member.students.map { student ->
                            val isSelf = userId == student.userId

                            AdvisoryMemberAdapter.Item(
                                student = student,
                                isSelf = isSelf,
                                isChatEnable = isChatEnable
                            )
                        }

                        addAll(students)
                    }
                }

                binding.vAdvisoryMember.updateRecyclerViewItems(items, newItems)
            }
        })
    }

    override fun observeError() {
        viewModel.memberErrorResponse.observe(this, Observer {
            it?.let {
                showApiResponseXAlert(activity, it)
            }
        })

        viewModel.errorMessage.observe(this) {
            it?.let {
                requireContext().showAlertWithOkButton(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
