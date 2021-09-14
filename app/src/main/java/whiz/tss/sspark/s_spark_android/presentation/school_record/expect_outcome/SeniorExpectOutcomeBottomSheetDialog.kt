package whiz.tss.sspark.s_spark_android.presentation.school_record.expect_outcome

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
import whiz.sspark.library.data.viewModel.ExpectOutcomeViewModel
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentSeniorExpectOutcomeBinding
import whiz.tss.sspark.s_spark_android.presentation.school_record.expect_outcome.info.SeniorExpectOutcomeInfoDialog

class SeniorExpectOutcomeBottomSheetDialog: BottomSheetDialogFragment() {

    companion object {
        private const val EXPECT_OUTCOME_INFO = "ExpectOutcomeInfo"

        fun newInstance(termId: String, courseId: String, courseCode: String, courseName: String, credit: Int) = SeniorExpectOutcomeBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("termId", termId)
                putString("courseId", courseId)
                putString("courseCode", courseCode)
                putString("courseName", courseName)
                putInt("credit", credit)
            }
        }
    }

    private val viewModel: ExpectOutcomeViewModel by viewModel()

    private var _binding: FragmentSeniorExpectOutcomeBinding? = null
    private val binding get() = _binding!!

    private val termId by lazy {
        arguments?.getString("termId") ?: ""
    }

    private val courseId by lazy {
        arguments?.getString("courseId") ?: ""
    }

    private val courseCode by lazy {
        arguments?.getString("courseCode") ?: ""
    }

    private val courseName by lazy {
        arguments?.getString("courseName") ?: ""
    }

    private val credit by lazy {
        arguments?.getInt("credit", 0) ?: 0
    }

    private val indicators by lazy {
        resources.getStringArray(R.array.school_record_senior_indicator).toList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSeniorExpectOutcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeView()
        observeData()
        observeError()

        dialog?.setOnShowListener {
            validateDialog()
        }

        viewModel.getExpectOutcome(courseId = courseId, termId = termId)
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
        }
    }

    private fun initView() {
        binding.vExpectOutcome.init(
            title = courseCode,
            subTitle = courseName,
            credit = credit,
            onCloseClicked = {
                dismiss()
            },
            onInfoClicked = {
                val isShowing = childFragmentManager.findFragmentByTag(EXPECT_OUTCOME_INFO) != null
                if (!isShowing) {
                    SeniorExpectOutcomeInfoDialog.newInstance().show(childFragmentManager, EXPECT_OUTCOME_INFO)
                }
            },
            onRefresh = {
                viewModel.getExpectOutcome(courseId = courseId, termId = termId)
            }
        )
    }

    private fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vExpectOutcome.setSwipeRefreshLayout(isLoading)
        }
    }

    private fun observeData() {
        viewModel.expectOutcomeResponse.observe(this) {
            it?.let {
                binding.vExpectOutcome.updateItem(expectOutcome = it, indicators = indicators)
            }
        }
    }

    private fun observeError() {
        viewModel.expectOutcomeErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(requireActivity(), it) {
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}