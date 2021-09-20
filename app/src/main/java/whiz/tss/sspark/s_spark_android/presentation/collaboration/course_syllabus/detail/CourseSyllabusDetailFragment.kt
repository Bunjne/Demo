package whiz.tss.sspark.s_spark_android.presentation.collaboration.course_syllabus.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.CourseSyllabusDTO
import whiz.sspark.library.data.viewModel.CourseSyllabusDetailViewModel
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.collaboration.course_syllabus.detail.CourseSyllabusAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentCourseSyllabusDetailBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment

class CourseSyllabusDetailFragment: BaseFragment() {

    companion object {
        fun newInstance(classGroupId: String, termId: String) = CourseSyllabusDetailFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
                putString("termId", termId)
            }
        }
    }

    private val viewModel: CourseSyllabusDetailViewModel by viewModel()

    private var _binding: FragmentCourseSyllabusDetailBinding? = null
    private val binding get() = _binding!!

    private val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    private val termId by lazy {
        arguments?.getString("termId") ?: ""
    }

    private var listener: OnCloseListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCourseSyllabusDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener = if (parentFragment != null) {
            parentFragment as OnCloseListener
        } else {
            activity as OnCloseListener
        }

        initView()

        viewModel.getCourseDetail(classGroupId, termId)
    }

    override fun initView() {
        binding.vDetail.init {
            viewModel.getCourseDetail(classGroupId, termId)
        }
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vDetail.setSwipeRefreshLayout(isLoading)
        }
    }

    override fun observeData() {
        viewModel.courseDetailResponse.observe(this) {
            it?.let {
                updateAdapterItem(it)
            }
        }
    }

    override fun observeError() {
        viewModel.courseDetailErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(requireActivity(), it) {
                    listener?.onClose()
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                requireActivity().showAlertWithOkButton(it) {
                    listener?.onClose()
                }
            }
        }
    }

    private fun updateAdapterItem(courseSyllabusDTO: CourseSyllabusDTO) {
        val items = mutableListOf<CourseSyllabusAdapter.Item>()

        if (courseSyllabusDTO.description.isNotBlank()) {
            val courseDescriptionTitle = resources.getString(R.string.course_syllabus_description_text)
            items.add(CourseSyllabusAdapter.Item(title = courseDescriptionTitle))
            items.add(CourseSyllabusAdapter.Item(courseDetail = courseSyllabusDTO.description))
        }

        if (courseSyllabusDTO.outcomes.isNotEmpty()) {
            val courseExpectOutcomeTitle = resources.getString(R.string.course_syllabus_expect_outcome_text)
            items.add(CourseSyllabusAdapter.Item(title = courseExpectOutcomeTitle))

            courseSyllabusDTO.outcomes.forEach {
                items.add(CourseSyllabusAdapter.Item(code = it.code , courseDetail = it.description))
            }
        }

        binding.vDetail.updateItem(items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnCloseListener {
        fun onClose()
    }
}