package whiz.sspark.library.view.screen.learning_outcome

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.GradeSummary
import whiz.sspark.library.data.entity.LearningOutcome
import whiz.sspark.library.data.entity.LearningOutcomeDTO
import whiz.sspark.library.databinding.ViewSeniorLearningOutcomeFragmentBinding
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.widget.learning_outcome.JuniorLearningOutcomeAdapter
import whiz.sspark.library.view.widget.learning_outcome.SeniorLearningOutcomeAdapter

class SeniorLearningOutcomeFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewSeniorLearningOutcomeFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onRefresh: () -> Unit,
             onItemClicked: (LearningOutcome) -> Unit) {
        binding.srlLearningOutcome.setOnRefreshListener {
            updateItem()
            onRefresh()
        }

        with(binding.rvLearningOutcome) {
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    CustomDividerMultiItemDecoration(
                        divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                        dividerViewType = listOf(JuniorLearningOutcomeAdapter.PROGRESS_BAR_TYPE, JuniorLearningOutcomeAdapter.UNDER_EVALUATION_TYPE)
                    )
                )
            }

            layoutManager = LinearLayoutManager(context)
            adapter = SeniorLearningOutcomeAdapter(context, onItemClicked)
        }
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlLearningOutcome.isRefreshing = isLoading == true
    }

    fun updateItem(learningOutcomes: List<LearningOutcomeDTO> = listOf()) {
        val item: MutableList<SeniorLearningOutcomeAdapter.Item> = mutableListOf()
        val filteredLearningOutcome = learningOutcomes.filter { it.value != null }

        val titleItem = SeniorLearningOutcomeAdapter.Item(title = resources.getString(R.string.school_record_grade_summary_text))
        val fullValue = filteredLearningOutcome.maxOfOrNull { it.fullValue } ?: 0f
        val gradeSummaries = filteredLearningOutcome.map {
            GradeSummary(
                name = it.name,
                startColorCode = it.colorCode1,
                endColorCode = it.colorCode2,
                grade = it.value!!)
        }

        item.add(titleItem)
        item.add(SeniorLearningOutcomeAdapter.Item(
            gradeSummaries = gradeSummaries,
            fullValue = fullValue
        ))

        filteredLearningOutcome.forEach { learningOutcome ->

            val titleItem = SeniorLearningOutcomeAdapter.Item(title = learningOutcome.name)
            item.add(titleItem)

            learningOutcome.courses.forEach {
                val startColor = if (learningOutcome.colorCode1.isNotBlank()) {
                    Color.parseColor(learningOutcome.colorCode1)
                } else {
                    ContextCompat.getColor(context, R.color.primaryStartColor)
                }

                val endColor = if (learningOutcome.colorCode1.isNotBlank() && learningOutcome.colorCode2.isNotBlank()) {
                    Color.parseColor(learningOutcome.colorCode2)
                } else {
                    if (learningOutcome.colorCode1.isNotBlank()) {
                        Color.parseColor(learningOutcome.colorCode1)
                    } else {
                        ContextCompat.getColor(context, R.color.primaryEndColor)
                    }
                }

                val learningOutcomeItem = SeniorLearningOutcomeAdapter.Item(
                    learningOutcome = LearningOutcome(
                        startColor = startColor,
                        endColor = endColor,
                        credit = it.credits,
                        percentPerformance = it.percentPerformance ?: 0,
                        courseCode = it.code,
                        courseName = it.name)
                )

                item.add(learningOutcomeItem)
            }
        }

        (binding.rvLearningOutcome.adapter as? SeniorLearningOutcomeAdapter)?.submitList(item)

        if (filteredLearningOutcome.isNotEmpty()) {
            binding.tvNoRecord.visibility = View.GONE
            binding.rvLearningOutcome.visibility = View.VISIBLE
        } else {
            binding.tvNoRecord.visibility = View.VISIBLE
            binding.rvLearningOutcome.visibility = View.GONE
        }
    }
}