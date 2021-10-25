package whiz.tss.sspark.s_spark_android.presentation.school_record.expect_outcome

import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.view.general.information_dialog.InformationDialogAdapter
import whiz.sspark.library.view.widget.school_record.expect_outcome.ExpectOutcomeAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.presentation.calendar.info_dialog.InformationDialog

open class SeniorExpectOutcomeBottomSheetDialog: JuniorExpectOutcomeBottomSheetDialog() {

    companion object {
        fun newInstance(termId: String, courseId: String, courseCode: String, courseName: String, credit: Int) = SeniorExpectOutcomeBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("termId", termId)
                putString("courseId", courseId)
                putString("courseCode", courseCode)
                putString("courseName", courseName)
                putInt("credit", credit)
            }
        }

        fun getOutcomesHeader(context: Context) = context.resources.getString(R.string.school_record_evaluation_title)

        fun getSeniorOutcomeItems(context: Context): List<InformationDialogAdapter.Item> = listOf(
            SeniorOutcomeIndex(
                level = context.resources.getString(R.string.school_record_beginning_text),
                description = context.resources.getString(R.string.school_record_beginning_description_text)
            ),
            SeniorOutcomeIndex(
                level = context.resources.getString(R.string.school_record_developing_text),
                description = context.resources.getString(R.string.school_record_developing_description_text)
            ),
            SeniorOutcomeIndex(
                level = context.resources.getString(R.string.school_record_proficient_text),
                description = context.resources.getString(R.string.school_record_proficient_description_text)
            ),
            SeniorOutcomeIndex(
                level = context.resources.getString(R.string.school_record_advance_text),
                description = context.resources.getString(R.string.school_record_advance_description_text)
            )
        ).toInformationItems()
    }

    override val indicators by lazy {
        resources.getStringArray(R.array.school_record_senior_indicator).toList()
    }

    override fun showInformationDialog() {
        InformationDialog.newInstance(
            headerText = getOutcomesHeader(requireContext()),
            informationItems = getSeniorOutcomeItems(requireContext())
        ).show(childFragmentManager, EXPECT_OUTCOME_INFO)
    }

    override fun updateAdapterItem(expectOutcome: ExpectOutcomeDTO) {
        val items: MutableList<ExpectOutcomeAdapter.Item> = mutableListOf()

        val startColor = expectOutcome.colorCode1.toColor(ContextCompat.getColor(requireContext(), R.color.primaryStartColor))
        val endColor = expectOutcome.colorCode2.toColor(ContextCompat.getColor(requireContext(), R.color.primaryEndColor))

        val instructorCommentItem = expectOutcome.instructorComment?.convertToAdapterItem()
        instructorCommentItem?.let {
            items.add(ExpectOutcomeAdapter.Item(title = resources.getString(R.string.school_record_instructor_comment_text)))
            items.add(ExpectOutcomeAdapter.Item(comment = instructorCommentItem))
        }

        if (expectOutcome.isCompleted || expectOutcome.outcomes.isNotEmpty()) {
            items.add(ExpectOutcomeAdapter.Item(title = resources.getString(R.string.school_record_progress_text)))
        }

        if (expectOutcome.isCompleted) {
            val convertedOverallValue = (expectOutcome.value?.div(expectOutcome.fullValue))?.times(indicators.size) ?: 0f

            val overall = ExpectOutcomeOverall(
                value = convertedOverallValue,
                startColor = startColor,
                endColor = endColor,
                indicators = indicators
            )

            items.add(ExpectOutcomeAdapter.Item(overall = overall))
        }

        expectOutcome.outcomes.forEach {
            val convertedValue = (it.value ?: 0 / (it.fullValue / indicators.size))

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

        binding.vExpectOutcome.updateItem(items)
    }
}