package whiz.tss.sspark.s_spark_android.presentation.collaboration.course_syllabus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import whiz.sspark.library.extension.toResourceColor
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentCourseSyllabusBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseBottomSheetDialogFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.course_syllabus.detail.CourseSyllabusDetailFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.course_syllabus.week_detail.CourseSyllabusWeekDetailFragment

class CourseSyllabusBottomSheetDialog: BaseBottomSheetDialogFragment(),
    CourseSyllabusDetailFragment.OnCloseListener,
    CourseSyllabusWeekDetailFragment.OnCloseListener {

    companion object {
        const val COURSE_DETAIL = 0
        const val COURSE_WEEK_DETAIL = 1

        fun newInstance(classGroupId: String, termId: String, startColor: Int, endColor: Int) = CourseSyllabusBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
                putString("termId", termId)
                putInt("startColor", startColor)
                putInt("endColor", endColor)
            }
        }
    }

    private var _binding: FragmentCourseSyllabusBinding? = null
    private val binding get() = _binding!!

    private val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    private val termId by lazy {
        arguments?.getString("termId") ?: ""
    }

    private val startColor by lazy {
        arguments?.getInt("startColor") ?: R.color.primaryStartColor.toResourceColor(requireContext())
    }

    private val endColor by lazy {
        arguments?.getInt("endColor") ?: R.color.primaryEndColor.toResourceColor(requireContext())
    }

    private val segmentTitles by lazy {
        resources.getStringArray(R.array.course_syllabus_segment).toList()
    }

    override val isDraggable = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCourseSyllabusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun initView() {
        binding.vCourseSyllabus.init(
            segmentTitles = segmentTitles,
            startColor = startColor,
            endColor = endColor,
            onTabClicked = {
                when (it) {
                    COURSE_DETAIL -> binding.vCourseSyllabus.renderFragment(CourseSyllabusDetailFragment.newInstance(classGroupId, termId), childFragmentManager, COURSE_DETAIL)
                    COURSE_WEEK_DETAIL -> binding.vCourseSyllabus.renderFragment(CourseSyllabusWeekDetailFragment.newInstance(classGroupId, termId), childFragmentManager, COURSE_WEEK_DETAIL)
                }
            },
            onCloseClicked = {
                dismiss()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClose() {
        dismiss()
    }
}