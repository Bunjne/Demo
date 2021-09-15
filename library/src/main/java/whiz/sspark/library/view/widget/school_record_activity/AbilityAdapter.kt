package whiz.sspark.library.view.widget.school_record_activity

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.AbilityItem
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.ItemListTitleView
import java.lang.IndexOutOfBoundsException

class AbilityAdapter: ListAdapter<AbilityAdapter.Item, RecyclerView.ViewHolder>(AbilityDiffCallback()) {

    companion object {
        private val TITLE_VIEW_TYPE = 1111
        private val PROGRESS_BAR_VIEW_TYPE = 2222
    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_VIEW_TYPE
        }

        return when {
            item.ability != null -> PROGRESS_BAR_VIEW_TYPE
            else -> TITLE_VIEW_TYPE
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class ProgressBarViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PROGRESS_BAR_VIEW_TYPE -> {
                ProgressBarViewHolder(AbilityItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            else -> {
                TitleViewHolder(ItemListTitleView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val viewType = getItemViewType(position)
        val isNextItemHeader = getItemViewType(position + 1) == TITLE_VIEW_TYPE
        val isPreviousItemHeader = getItemViewType(position - 1) == TITLE_VIEW_TYPE

        when(viewType) {
            PROGRESS_BAR_VIEW_TYPE -> {
                (holder.itemView as? AbilityItemView)?.apply {
                    init(item.ability!!)
                    setDarkModeBackground(isNextItemHeader, isPreviousItemHeader)
                }
            }
            else -> {
                (holder.itemView as? ItemListTitleView)?.apply {
                    init(item.title ?: "")
                }
            }
        }
    }

    data class Item(
        val title: String? = null,
        val ability: AbilityItem? = null
    )
}

private class AbilityDiffCallback : DiffUtil.ItemCallback<AbilityAdapter.Item>() {
    override fun areItemsTheSame(oldItem: AbilityAdapter.Item, newItem: AbilityAdapter.Item): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: AbilityAdapter.Item, newItem: AbilityAdapter.Item): Boolean {
        return oldItem.title == newItem.title && oldItem.ability == newItem.ability

    }
}