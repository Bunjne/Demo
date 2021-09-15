package whiz.sspark.library.view.screen.expect_outcome

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ExpectOutcomeCourseItem
import whiz.sspark.library.data.entity.ExpectOutcomeDTO
import whiz.sspark.library.data.entity.ExpectOutcomeOverallItem
import whiz.sspark.library.databinding.ViewSeniorExpectOutcomeFragmentBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.view.widget.expect_outcome.ExpectOutcomeAdapter

class SeniorExpectOutcomeFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewSeniorExpectOutcomeFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String,
             subTitle: String,
             credit: Int,
             onCloseClicked: () -> Unit,
             onInfoClicked: () -> Unit,
             onRefresh: () -> Unit) {
        binding.ivInfo.show(R.drawable.ic_info)
        binding.ivClose.show(R.drawable.ic_close_bottom_sheet_dialog)

        binding.tvTitle.text = title
        binding.tvCredit.text = resources.getString(R.string.general_credit, credit.toString())
        binding.tvSubTitle.text = subTitle

        with(binding.rvExpectOutcome) {
            layoutManager = LinearLayoutManager(context)
            adapter = ExpectOutcomeAdapter(context)
        }

        binding.srlContainer.setOnRefreshListener {
            onRefresh()
        }

        binding.ivInfo.setOnClickListener {
            onInfoClicked()
        }

        binding.ivClose.setOnClickListener {
            onCloseClicked()
        }
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun updateItem(expectOutcome: ExpectOutcomeDTO, indicators: List<String>) {
        val items: MutableList<ExpectOutcomeAdapter.Item> = mutableListOf()

        val startColor = expectOutcome.colorCode1.toColor(ContextCompat.getColor(context, R.color.primaryStartColor))
        val endColor = expectOutcome.colorCode2.toColor(ContextCompat.getColor(context, R.color.primaryEndColor))

        val instructorCommentItem = expectOutcome.instructorComment?.convertToAdapterItem()
        instructorCommentItem?.let {
            items.add(ExpectOutcomeAdapter.Item(title = resources.getString(R.string.school_record_instructor_comment_text)))
            items.add(ExpectOutcomeAdapter.Item(commentItem = instructorCommentItem))
        }

        if (expectOutcome.isCompleted || expectOutcome.outcomes.isNotEmpty()) {
            items.add(ExpectOutcomeAdapter.Item(title = resources.getString(R.string.school_record_progress_text)))
        }

        if (expectOutcome.isCompleted) {
            val convertedOverAllValue = (expectOutcome.value ?: 0 / expectOutcome.fullValue) * indicators.size

            val overAllItem = ExpectOutcomeOverallItem(
                value = convertedOverAllValue,
                startColor = startColor,
                endColor = endColor,
                indicators = indicators
            )

            items.add(ExpectOutcomeAdapter.Item(overAllItem = overAllItem))
        }

        expectOutcome.outcomes.forEach {
            val convertedValue = (it.value ?: 0 / (it.fullValue / indicators.size))

            val courseItem = ExpectOutcomeCourseItem(
                title = it.code,
                description = it.description,
                value = convertedValue,
                startColor = startColor,
                endColor = endColor,
                indicators = indicators
            )

            items.add(ExpectOutcomeAdapter.Item(courseItem = courseItem))
        }

        (binding.rvExpectOutcome.adapter as? ExpectOutcomeAdapter)?.submitList(items)
    }
}
