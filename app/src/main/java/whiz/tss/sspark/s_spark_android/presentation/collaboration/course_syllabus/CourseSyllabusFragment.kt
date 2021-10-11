package whiz.tss.sspark.s_spark_android.presentation.collaboration.course_syllabus

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.extension.toResourceColor
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentCourseSyllabusBinding
import whiz.tss.sspark.s_spark_android.presentation.collaboration.course_syllabus.detail.CourseSyllabusDetailFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.course_syllabus.week_detail.CourseSyllabusWeekDetailFragment

class CourseSyllabusFragment: DialogFragment(),
    CourseSyllabusDetailFragment.OnCloseListener,
    CourseSyllabusWeekDetailFragment.OnCloseListener {

    companion object {
        const val COURSE_DETAIL = 0
        const val COURSE_WEEK_DETAIL = 1

        fun newInstance(classGroupId: String, termId: String, startColor: Int, endColor: Int) = CourseSyllabusFragment().apply {
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentCourseSyllabusBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext()).create().apply {
            setView(binding.root)
            setCancelable(false)
            setDialogAnimation(window)
        }

        initView()

        return dialog
    }

    private fun setDialogAnimation(window: Window?) {
        window?.let {
            with(window) {
                decorView.setBackgroundColor(Color.TRANSPARENT)
                setGravity(Gravity.BOTTOM)
                attributes.windowAnimations = R.style.VerticalSlidesAnimationStyle
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    private fun initView() {
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

    override fun onStart() {
        super.onStart()

        val height = resources.displayMetrics.heightPixels - 20.toDP(requireContext())
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            height
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