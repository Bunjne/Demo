package whiz.tss.sspark.s_spark_android.presentation.school_record.expect_outcome

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.viewModel.ExpectOutcomeViewModel
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.utility.isPrimaryHighSchool
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.general.information_dialog.InformationDialogAdapter
import whiz.sspark.library.view.widget.school_record.expect_outcome.ExpectOutcomeAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentJuniorExpectOutcomeBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseBottomSheetDialogFragment
import whiz.tss.sspark.s_spark_android.presentation.calendar.info_dialog.InformationDialog

open class JuniorExpectOutcomeBottomSheetDialog: BaseBottomSheetDialogFragment() {

    companion object {
        internal const val EXPECT_OUTCOME_INFO = "ExpectOutcomeInfo"

        fun newInstance(termId: String, courseId: String, courseCode: String, courseName: String, credit: Int) = JuniorExpectOutcomeBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("termId", termId)
                putString("courseId", courseId)
                putString("courseCode", courseCode)
                putString("courseName", courseName)
                putInt("credit", credit)
            }
        }

        fun getOutcomesHeader(context: Context) = context.resources.getString(R.string.school_record_evaluation_title)

        fun getJuniorOutcomesItems(context: Context): List<InformationDialogAdapter.Item> = listOf(
            JuniorOutcomeIndex(0, context.resources.getString(R.string.school_record_zero_score_description_text)),
            JuniorOutcomeIndex(1, context.resources.getString(R.string.school_record_one_score_description_text)),
            JuniorOutcomeIndex(2, context.resources.getString(R.string.school_record_two_score_description_text)),
            JuniorOutcomeIndex(3, context.resources.getString(R.string.school_record_three_score_description_text)),
            JuniorOutcomeIndex(4, context.resources.getString(R.string.school_record_four_score_description_text)),
            JuniorOutcomeIndex(5, context.resources.getString(R.string.school_record_five_score_description_text)),
        ).toInformationItems()
    }

    protected open val viewModel: ExpectOutcomeViewModel by viewModel()

    private var _binding: FragmentJuniorExpectOutcomeBinding? = null
    protected val binding get() = _binding!!

    protected val termId by lazy {
        arguments?.getString("termId") ?: ""
    }

    protected val courseId by lazy {
        arguments?.getString("courseId") ?: ""
    }

    protected val courseCode by lazy {
        arguments?.getString("courseCode") ?: ""
    }

    protected val courseName by lazy {
        arguments?.getString("courseName") ?: ""
    }

    protected val credit by lazy {
        arguments?.getInt("credit", 0) ?: 0
    }

    protected open val indicators by lazy {
        resources.getStringArray(R.array.school_record_junior_indicator).toList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentJuniorExpectOutcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        dialog?.setOnShowListener {
            validateDialog()
        }

        getExpectOutcome()
    }

    protected open fun getExpectOutcome() {
        viewModel.getExpectOutcome(
            courseId = courseId,
            termId = termId
        )
    }

    protected open fun showInformationDialog() {
        InformationDialog.newInstance(
            headerText = getOutcomesHeader(requireContext()),
            informationItems = getJuniorOutcomesItems(requireContext())
        ).show(childFragmentManager, EXPECT_OUTCOME_INFO)
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

    override fun initView() {
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
                    showInformationDialog()
                }
            },
            onRefresh = {
                getExpectOutcome()
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vExpectOutcome.setSwipeRefreshLayout(isLoading)
        }
    }

    override fun observeData() {
        viewModel.expectOutcomeResponse.observe(this) {
            it?.let {
                updateAdapterItem(it)
            }
        }
    }

    protected open fun updateAdapterItem(expectOutcome: ExpectOutcomeDTO) {
        val items: MutableList<ExpectOutcomeAdapter.Item> = mutableListOf()

        val startColor = expectOutcome.colorCode1.toColor(ContextCompat.getColor(requireContext(), R.color.primaryStartColor))
        val endColor = expectOutcome.colorCode2.toColor(ContextCompat.getColor(requireContext(), R.color.primaryEndColor))

        val instructorCommentItem = expectOutcome.instructorComment?.convertToAdapterItem()
        instructorCommentItem?.let {
            items.add(ExpectOutcomeAdapter.Item(title = resources.getString(R.string.school_record_instructor_comment_text)))
            items.add(ExpectOutcomeAdapter.Item(comment = instructorCommentItem))
        }

        if (expectOutcome.outcomes.isNotEmpty()) {
            items.add(ExpectOutcomeAdapter.Item(title = resources.getString(R.string.school_record_expect_text)))

            expectOutcome.outcomes.forEach {
                val convertedValue = (it.value?.div(it.fullValue))?.times(indicators.size) ?: 0f

                val course = ExpectOutcomeCourse(
                    title = it.code,
                    description = it.description,
                    value = convertedValue,
                    startColor = startColor,
                    endColor = endColor,
                    indicators = indicators
                )

                items.add(ExpectOutcomeAdapter.Item(course = course))
            }
        }

        binding.vExpectOutcome.updateItem(items)
    }

    override fun observeError() {
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