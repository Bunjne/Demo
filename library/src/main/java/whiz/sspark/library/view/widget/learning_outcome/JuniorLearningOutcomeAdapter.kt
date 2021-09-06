package whiz.sspark.library.view.widget.learning_outcome

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.LearningOutcome
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.ItemListTitleView

class JuniorLearningOutcomeAdapter(private val context: Context,
                                   private val onItemClicked: () -> Unit): ListAdapter<JuniorLearningOutcomeAdapter.Item, RecyclerView.ViewHolder>(JuniorLearningOutcomeDiffCallback()) {

    companion object {
        const val TITLE_TYPE = 1111
        const val PROGRESS_BAR_TYPE = 2222
        const val UNDER_EVALUATION_TYPE = 3333
    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_TYPE
        }

        return if (item.learningOutcome != null) {
            if (item.learningOutcome.percentPerformance != null) {
                PROGRESS_BAR_TYPE
            } else {
                UNDER_EVALUATION_TYPE
            }
        } else {
            TITLE_TYPE
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class ProgressBarViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class UnderEvaluationViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PROGRESS_BAR_TYPE -> {
                ProgressBarViewHolder(LearningOutcomeProgressBarItemView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            UNDER_EVALUATION_TYPE -> {
                UnderEvaluationViewHolder(LearningOutcomeUnderEvaluationView(context).apply {
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
        item?.let {
            val viewType = getItemViewType(position)
            val isNextItemHeader = getItemViewType(position + 1) == TITLE_TYPE
            val isPreviousItemHeader = getItemViewType(position - 1) == TITLE_TYPE

            when(viewType) {
                PROGRESS_BAR_TYPE -> {
                    (holder.itemView as? LearningOutcomeProgressBarItemView)?.apply {
                        init(it.learningOutcome!!)
                        setDarkModeBackground(isNextItemHeader, isPreviousItemHeader)

                        setOnClickListener {
                            onItemClicked()
                        }
                    }
                }
                UNDER_EVALUATION_TYPE -> {
                    (holder.itemView as? LearningOutcomeUnderEvaluationView)?.apply {
                        init(it.learningOutcome!!)
                        setDarkModeBackground(isNextItemHeader, isPreviousItemHeader)

                        setOnClickListener {
                            onItemClicked()
                        }
                    }
                }
                else -> {
                    (holder.itemView as? ItemListTitleView)?.apply {
                        init(it.title!!)
                    }
                }
            }
        }
    }

    data class Item(
        val title: String? = null,
        val learningOutcome: LearningOutcome? = null
    )
}

private class JuniorLearningOutcomeDiffCallback : DiffUtil.ItemCallback<JuniorLearningOutcomeAdapter.Item>() {
    override fun areItemsTheSame(oldItem: JuniorLearningOutcomeAdapter.Item, newItem: JuniorLearningOutcomeAdapter.Item): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: JuniorLearningOutcomeAdapter.Item, newItem: JuniorLearningOutcomeAdapter.Item): Boolean {
        return oldItem == newItem
    }
}