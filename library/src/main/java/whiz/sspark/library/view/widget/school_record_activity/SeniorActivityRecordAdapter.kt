package whiz.sspark.library.view.widget.school_record_activity

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.ActivityRecordProgressItem
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.ItemListTitleView
import java.lang.IndexOutOfBoundsException

class SeniorActivityRecordAdapter: ListAdapter<SeniorActivityRecordAdapter.Item, RecyclerView.ViewHolder>(SeniorActivityRecordDiffCallback()) {

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
            item.activityRecordItem != null -> PROGRESS_BAR_VIEW_TYPE
            else -> TITLE_VIEW_TYPE
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class ProgressBarViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PROGRESS_BAR_VIEW_TYPE -> {
                ProgressBarViewHolder(SeniorActivityRecordItemView(parent.context).apply {
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
        item?.let {
            val viewType = getItemViewType(position)
            val isNextItemHeader = getItemViewType(position + 1) == TITLE_VIEW_TYPE
            val isPreviousItemHeader = getItemViewType(position - 1) == TITLE_VIEW_TYPE

            when(viewType) {
                PROGRESS_BAR_VIEW_TYPE -> {
                    (holder.itemView as? SeniorActivityRecordItemView)?.apply {
                        init(it.activityRecordItem!!)
                        setDarkModeBackground(isNextItemHeader, isPreviousItemHeader)
                    }
                }
                else -> {
                    (holder.itemView as? ItemListTitleView)?.apply {
                        init(it.title ?: "")
                    }
                }
            }
        }
    }

    data class Item(
        val title: String? = null,
        val activityRecordItem: ActivityRecordProgressItem? = null
    )
}

private class SeniorActivityRecordDiffCallback : DiffUtil.ItemCallback<SeniorActivityRecordAdapter.Item>() {
    override fun areItemsTheSame(oldItem: SeniorActivityRecordAdapter.Item, newItem: SeniorActivityRecordAdapter.Item): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: SeniorActivityRecordAdapter.Item, newItem: SeniorActivityRecordAdapter.Item): Boolean {
        return oldItem.title == newItem.title && oldItem.activityRecordItem == newItem.activityRecordItem

    }
}