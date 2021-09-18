package whiz.tss.sspark.s_spark_android.presentation.learning_pathway.add_course

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.viewModel.AddCourseViewModel
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentAddCourseBinding

class AddCourseBottomSheetDialog: BottomSheetDialogFragment() {
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
        observeView()
        observeData()
        observeError()

        dialog?.setOnShowListener {
            validateDialog()
        }

        viewModel.getConcentrateCourse()
    }

    private fun validateDialog() {
        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.let {
            val bottomSheet = dialog!!.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet.setBackgroundColor(Color.TRANSPARENT)
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

            bottomSheet.parent.parent.requestLayout()
        }
    }

    private fun initView() {
        binding.vAddCourse.init(
            term = term,
            selectedCourseIds = selectedCourseIds,
            currentCredit = currentCredit,
            minCredit = minCredit,
            maxCredit = maxCredit,
            onAddClicked = { courseId ->
                listener?.onAddCourseClicked(term.id, courseId)
                dismiss()
            },
            onRefresh = {
                viewModel.getConcentrateCourse()
            }
        )
    }

    private fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vAddCourse.setSwipeRefreshLoading(isLoading)
        }
    }

    private fun observeData() {
        viewModel.concentrateCourseResponse.observe(this) {
            it?.let {
                binding.vAddCourse.updateConcentrateCourses(it)
            }
        }
    }

    private fun observeError() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnClickListener {
        fun onAddCourseClicked(termId: String, courseId: String)
    }
}