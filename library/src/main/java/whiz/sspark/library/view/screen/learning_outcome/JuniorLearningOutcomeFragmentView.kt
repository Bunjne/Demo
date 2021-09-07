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
import whiz.sspark.library.data.entity.LearningOutcome
import whiz.sspark.library.data.entity.LearningOutcomeDTO
import whiz.sspark.library.databinding.ViewJuniorLearningOutcomeFragmentBinding
import whiz.sspark.library.view.widget.general.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.widget.learning_outcome.JuniorLearningOutcomeAdapter

class JuniorLearningOutcomeFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewJuniorLearningOutcomeFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onRefresh: () -> Unit,
             onItemClicked: () -> Unit) {
        binding.srlLearningOutcome.setOnRefreshListener {
            updateItem()
            onRefresh()
        }

        with(binding.rvLearningOutcome) {
            if (itemDecorationCount == 0) {
                addItemDecoration(CustomDividerMultiItemDecoration(
                    divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                    dividerViewType = listOf(JuniorLearningOutcomeAdapter.PROGRESS_BAR_TYPE, JuniorLearningOutcomeAdapter.UNDER_EVALUATION_TYPE)
                ))
            }

            layoutManager = LinearLayoutManager(context)
            adapter = JuniorLearningOutcomeAdapter(context, onItemClicked)
        }
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlLearningOutcome.isRefreshing = isLoading == true
    }

    fun updateItem(learningOutcomes: List<LearningOutcomeDTO> = listOf()) {
        val item: MutableList<JuniorLearningOutcomeAdapter.Item> = mutableListOf()

        learningOutcomes.forEach { learningOutcome ->
            val titleItem = JuniorLearningOutcomeAdapter.Item(title = learningOutcome.header)
            item.add(titleItem)

            learningOutcome.courses.forEach {
                val startColor = if (learningOutcome.gradientColor1.isNotBlank()) {
                    Color.parseColor(learningOutcome.gradientColor1)
                } else {
                    ContextCompat.getColor(context, R.color.primaryStartColor)
                }

                val endColor = if (learningOutcome.gradientColor1.isNotBlank() && learningOutcome.gradientColor2.isNotBlank()) {
                    Color.parseColor(learningOutcome.gradientColor2)
                } else {
                    if (learningOutcome.gradientColor1.isNotBlank()) {
                        Color.parseColor(learningOutcome.gradientColor1)
                    } else {
                        ContextCompat.getColor(context, R.color.primaryEndColor)
                    }
                }

                val learningOutcomeItem = JuniorLearningOutcomeAdapter.Item(
                    learningOutcome = LearningOutcome(
                        startColor = startColor,
                        endColor = endColor,
                        credit = it.credits,
                        percentPerformance = it.percentPerformance,
                        courseCode = it.nameEn,
                        courseName = it.nameTh)
                )

                item.add(learningOutcomeItem)
            }
        }

        (binding.rvLearningOutcome.adapter as? JuniorLearningOutcomeAdapter)?.submitList(item)

        if (learningOutcomes.isNotEmpty()) {
            binding.tvNoRecord.visibility = View.GONE
            binding.rvLearningOutcome.visibility = View.VISIBLE
        } else {
            binding.tvNoRecord.visibility = View.VISIBLE
            binding.rvLearningOutcome.visibility = View.GONE
        }
    }
}