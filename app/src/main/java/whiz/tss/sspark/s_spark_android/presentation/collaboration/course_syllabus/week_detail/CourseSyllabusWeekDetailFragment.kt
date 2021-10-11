package whiz.tss.sspark.s_spark_android.presentation.collaboration.course_syllabus.week_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.CourseSyllabusDTO
import whiz.sspark.library.data.viewModel.CourseSyllabusViewModel
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.collaboration.course_syllabus.week.CourseSyllabusWeekAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentCourseSyllabusWeekDetailBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment

class CourseSyllabusWeekDetailFragment: BaseFragment() {

    companion object {
        fun newInstance(classGroupId: String, termId: String) = CourseSyllabusWeekDetailFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
                putString("termId", termId)
            }
        }
    }

    private val viewModel: CourseSyllabusViewModel by viewModel()

    private var _binding: FragmentCourseSyllabusWeekDetailBinding? = null
    private val binding get() = _binding!!

    private val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    private val termId by lazy {
        arguments?.getString("termId") ?: ""
    }

    private var listener: OnCloseListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCourseSyllabusWeekDetailBinding.inflate(inflater, container, false)
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
        binding.vWeekDetail.init {
            viewModel.getCourseDetail(classGroupId, termId)
        }
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vWeekDetail.setSwipeRefreshLayout(isLoading)
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
        val items = mutableListOf<CourseSyllabusWeekAdapter.Item>()

        courseSyllabusDTO.syllabus.forEach {
            val week = resources.getString(R.string.course_syllabus_week, it.week.toString())
            items.add(CourseSyllabusWeekAdapter.Item(title = week))

            it.topics.forEach {
                items.add(CourseSyllabusWeekAdapter.Item(courseDetail = it))
            }

            val instructors = it.instructors.map { it.firstNameWithPosition }
            items.add(CourseSyllabusWeekAdapter.Item(instructors = instructors))
        }

        binding.vWeekDetail.updateItem(items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnCloseListener {
        fun onClose()
    }
}