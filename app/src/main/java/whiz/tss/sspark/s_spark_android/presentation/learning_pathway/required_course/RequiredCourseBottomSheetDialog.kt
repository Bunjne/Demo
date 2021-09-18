package whiz.tss.sspark.s_spark_android.presentation.learning_pathway.required_course

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import whiz.sspark.library.data.entity.Course
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentRequiredCourseBinding

class RequiredCourseBottomSheetDialog: BottomSheetDialogFragment() {
    companion object {

        fun newInstance(term: Term, courses: List<Course>) = RequiredCourseBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("term", term.toJson())
                putString("courses", courses.toJson())
            }
        }
    }

    private var _binding: FragmentRequiredCourseBinding? = null
    private val binding get() = _binding!!

    private val term by lazy {
        arguments?.getString("term")?.toObject<Term>() ?: Term()
    }

    private val courses by lazy {
        arguments?.getString("courses")?.toObjects(Array<Course>::class.java) ?: listOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRequiredCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        dialog?.setOnShowListener {
            validateDialog()
        }
    }

    private fun validateDialog() {
        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.let {
            val bottomSheet = dialog!!.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet.setBackgroundColor(Color.TRANSPARENT)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            BottomSheetBehavior.from(bottomSheet).run {

                state = BottomSheetBehavior.STATE_EXPANDED

                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(p0: View, p1: Float) { }

                    override fun onStateChanged(bottomSheet: View, state: Int) {
                        if (state == BottomSheetBehavior.STATE_COLLAPSED) {
                            dismiss()
                        }
                    }
                })
            }

            view?.requestLayout()
        }
    }

    private fun initView() {
        binding.vRequiredCourse.init(
            term = term,
            courses = courses,
            onCloseClicked = {
                dismiss()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}