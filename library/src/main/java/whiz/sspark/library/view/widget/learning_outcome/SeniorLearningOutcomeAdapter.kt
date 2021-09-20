package whiz.sspark.library.view.widget.learning_outcome

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.GradeSummary
import whiz.sspark.library.data.entity.LearningOutcome
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.ItemListTitleView

class SeniorLearningOutcomeAdapter(private val context: Context,
                                   private val onItemClicked: (LearningOutcome) -> Unit): ListAdapter<SeniorLearningOutcomeAdapter.Item, RecyclerView.ViewHolder>(SeniorLearningOutcomeDiffCallback()) {

    companion object {
        const val TITLE_TYPE = 1111
        const val COURSE_TYPE = 2222
        const val GRADE_SUMMARY_TYPE = 3333
    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_TYPE
        }

        return when {
            item.learningOutcome != null -> {
                COURSE_TYPE
            }
            item.gradeSummaries != null -> {
                GRADE_SUMMARY_TYPE
            }
            else -> {
                TITLE_TYPE
            }
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class GradeSummaryViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            COURSE_TYPE -> {
                CourseViewHolder(LearningOutcomeView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            GRADE_SUMMARY_TYPE -> {
                GradeSummaryViewHolder(LearningOutcomeGradeSummaryItemView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            else -> {
                TitleViewHolder(ItemListTitleView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val viewType = getItemViewType(position)
        val isNextItemHeader = getItemViewType(position + 1) == TITLE_TYPE
        val isPreviousItemHeader = getItemViewType(position - 1) == TITLE_TYPE

        when(viewType) {
            COURSE_TYPE -> {
                (holder.itemView as? LearningOutcomeView)?.apply {
                    init(item.learningOutcome!!)
                    setDarkModeBackground(isNextItemHeader, isPreviousItemHeader)

                    setOnClickListener {
                        onItemClicked(item.learningOutcome)
                    }
                }
            }
            GRADE_SUMMARY_TYPE -> {
                (holder.itemView as? LearningOutcomeGradeSummaryItemView)?.init(item.gradeSummaries!!)
            }
            else -> {
                (holder.itemView as? ItemListTitleView)?.init(item.title!!)
            }
        }
    }

    data class Item(
        val title: String? = null,
        val learningOutcome: LearningOutcome? = null,
        val gradeSummaries: List<GradeSummary>? = null,
    )
}

private class SeniorLearningOutcomeDiffCallback : DiffUtil.ItemCallback<SeniorLearningOutcomeAdapter.Item>() {
    override fun areItemsTheSame(oldItem: SeniorLearningOutcomeAdapter.Item, newItem: SeniorLearningOutcomeAdapter.Item): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: SeniorLearningOutcomeAdapter.Item, newItem: SeniorLearningOutcomeAdapter.Item): Boolean {
        return oldItem == newItem
    }
}