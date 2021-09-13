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
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentSeniorExpectOutcomeBinding
import whiz.tss.sspark.s_spark_android.presentation.school_record.expect_outcome.info.SeniorExpectOutcomeInfoDialog

class SeniorExpectOutcomeBottomSheetDialog: BottomSheetDialogFragment() {

    companion object {
        private const val EXPECT_OUTCOME_INFO = "ExpectOutcomeInfo"

        fun newInstance(title: String, subTitle: String, credit: Int) = SeniorExpectOutcomeBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("courseCode", courseCode)
                putString("courseName", courseName)
                putInt("credit", credit)
            }
        }
    }

    private var _binding: FragmentSeniorExpectOutcomeBinding? = null
    private val binding get() = _binding!!

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
        resources.getStringArray(R.array.school_record_senior_indicator)
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
                            dialog?.dismiss()
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
                dialog?.dismiss()
            },
            onInfoClicked = {
                val isShowing = childFragmentManager.findFragmentByTag(EXPECT_OUTCOME_INFO) != null
                if (!isShowing) {
                    SeniorExpectOutcomeInfoDialog.newInstance().show(childFragmentManager, EXPECT_OUTCOME_INFO)
                }
            },
            onRefresh = {
                //TODO wait implement API
            }
        )
    }

    private fun observeView() {

    }

    private fun observeData() {

    }

    private fun observeError() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}