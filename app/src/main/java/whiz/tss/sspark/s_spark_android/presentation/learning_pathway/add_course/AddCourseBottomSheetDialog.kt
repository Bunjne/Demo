package whiz.tss.sspark.s_spark_android.presentation.learning_pathway.add_course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.ConcentrateCourseDTO
import whiz.sspark.library.data.entity.Course
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.viewModel.AddCourseViewModel
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.learning_pathway.add_course.AddCourseAdapter
import whiz.tss.sspark.s_spark_android.databinding.FragmentAddCourseBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseBottomSheetDialogFragment

class AddCourseBottomSheetDialog: BaseBottomSheetDialogFragment() {
    companion object {
        fun newInstance(term: Term, currentCredit: Int, minCredit: Int, maxCredit: Int, selectedCourseIds: List<String>) = AddCourseBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("term", term.toJson())
                putInt("currentCredit", currentCredit)
                putInt("minCredit", minCredit)
                putInt("maxCredit", maxCredit)
                putString("selectedCourseIds", selectedCourseIds.toJson())
            }
        }
    }

    private val viewModel: AddCourseViewModel by viewModel()

    private var _binding: FragmentAddCourseBinding? = null
    private val binding get() = _binding!!

    private val term by lazy {
        arguments?.getString("term")?.toObject<Term>() ?: Term()
    }

    private val currentCredit by lazy {
        arguments?.getInt("currentCredit", 0) ?: 0
    }

    private val minCredit by lazy {
        arguments?.getInt("minCredit", 0) ?: 0
    }

    private val maxCredit by lazy {
        arguments?.getInt("maxCredit", 0) ?: 0
    }

    private val selectedCourseIds by lazy {
        arguments?.getString("selectedCourseIds")?.toObjects(Array<String>::class.java) ?: listOf()
    }

    private var concentrateCourses: MutableList<ConcentrateCourseDTO> = mutableListOf()
    private var currentSearchText = ""

    private var listener: OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener = if (parentFragment != null) {
            parentFragment as OnClickListener
        } else {
            activity as OnClickListener
        }

        initView()

        viewModel.getConcentrateCourse()
    }

    override fun initView() {
        binding.vAddCourse.init(
            term = term,
            currentCredit = currentCredit,
            minCredit = minCredit,
            maxCredit = maxCredit,
            onAddClicked = { courseId ->
                listener?.onAddCourseClicked(term.term, term.academicGrade ?: 0, courseId)
                dismiss()
            },
            onSearch = {
                currentSearchText = it
                updateAdapterItem()
            },
            onRefresh = {
                viewModel.getConcentrateCourse()
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vAddCourse.setSwipeRefreshLoading(isLoading)
        }
    }

    override fun observeData() {
        viewModel.concentrateCourseResponse.observe(this) {
            it?.let {
                with(concentrateCourses) {
                    clear()
                    addAll(it)
                }

                updateAdapterItem()
            }
        }
    }

    override fun observeError() {
        viewModel.concentrateCourseErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(requireActivity(), it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                requireActivity().showAlertWithOkButton(it)
            }
        }
    }

    private fun updateAdapterItem() {
        val items: MutableList<AddCourseAdapter.Item> = mutableListOf()

        concentrateCourses.forEach {
            val matchedCourses = if (currentSearchText.isNotBlank()) {
                it.courses.filter {
                    it.nameEn.contains(currentSearchText, true) ||
                    it.nameTh.contains(currentSearchText, true) ||
                    it.code.contains(currentSearchText, true)
                }
            } else {
                it.courses
            }

            if (matchedCourses.any()) {
                items.add(AddCourseAdapter.Item(title = it.name))

                val courses = matchedCourses.map {
                    val course = Course(
                        id = it.id,
                        code = it.code,
                        credit = it.credit,
                        name = it.name
                    )

                    val isSelectable = !selectedCourseIds.contains(course.id)

                    AddCourseAdapter.Item(course = course, isSelectable = isSelectable)
                }

                items.addAll(courses)
            }
        }

        binding.vAddCourse.updateAdapterItem(items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnClickListener {
        fun onAddCourseClicked(term: Int, academicGrade: Int, courseId: String)
    }
}