package whiz.sspark.library.view.widget.school_record.activity_record

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.ActivityRecord
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.ItemListTitleView
import java.lang.IndexOutOfBoundsException

class ActivityRecordAdapter: ListAdapter<ActivityRecordAdapter.Item, RecyclerView.ViewHolder>(
    ActivityRecordDiffCallback()
) {

    companion object {
        private val TITLE_VIEW_TYPE = 1111
        val STATUS_WITH_DESCRIPTON_VIEW_TYPE = 2222
        val STATUS_VIEW_TYPE = 3333
        val TITLE_WITH_DESCRIPTION_VIEW_TYPE = 4444
    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_VIEW_TYPE
        }

        return when {
            item.activity?.isCompleted != null && !item.activity.description.isNullOrBlank() -> STATUS_WITH_DESCRIPTON_VIEW_TYPE
            item.activity?.isCompleted != null -> STATUS_VIEW_TYPE
            !item.activity?.description.isNullOrBlank() -> TITLE_WITH_DESCRIPTION_VIEW_TYPE
            else -> TITLE_VIEW_TYPE
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class StatusWithDescriptionViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class StatusViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class TitleWithDescriptionViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            STATUS_WITH_DESCRIPTON_VIEW_TYPE -> {
                StatusWithDescriptionViewHolder(ActivityRecordStatusWithDescriptionItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            STATUS_VIEW_TYPE -> {
                StatusViewHolder(ActivityRecordStatusItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            TITLE_WITH_DESCRIPTION_VIEW_TYPE -> {
                TitleWithDescriptionViewHolder(ActivityRecordTitleWithDescriptionItemView(parent.context).apply {
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
            STATUS_WITH_DESCRIPTON_VIEW_TYPE -> {
                (holder.itemView as? ActivityRecordStatusWithDescriptionItemView)?.apply {
                    init(
                        isCompleted = item.activity?.isCompleted ?: false,
                        title = item.activity?.title ?: "",
                        description = item.activity?.description ?: ""
                    )

                    setDarkModeBackground(isNextItemHeader, isPreviousItemHeader)
                }
            }
            STATUS_VIEW_TYPE -> {
                (holder.itemView as? ActivityRecordStatusItemView)?.apply {
                    init(
                        isCompleted = item.activity?.isCompleted ?: false,
                        title = item.activity?.title ?: ""
                    )

                    setDarkModeBackground(isNextItemHeader, isPreviousItemHeader)
                }
            }
            TITLE_WITH_DESCRIPTION_VIEW_TYPE -> {
                (holder.itemView as? ActivityRecordTitleWithDescriptionItemView)?.apply {
                    init(
                        title = item.activity?.title ?: "",
                        description = item.activity?.description ?: ""
                    )

                    setDarkModeBackground(isNextItemHeader, isPreviousItemHeader)
                }
            }
            else -> {
                (holder.itemView as? ItemListTitleView)?.init(item.title ?: "")
            }
        }
    }

    data class Item(
        val title: String? = null,
        val activity: ActivityRecord? = null
    )
}

private class ActivityRecordDiffCallback : DiffUtil.ItemCallback<ActivityRecordAdapter.Item>() {
    override fun areItemsTheSame(oldItem: ActivityRecordAdapter.Item, newItem: ActivityRecordAdapter.Item): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ActivityRecordAdapter.Item, newItem: ActivityRecordAdapter.Item): Boolean {
        return oldItem.title == newItem.title &&
                oldItem.activity?.description == newItem.activity?.description &&
                oldItem.activity?.isCompleted == newItem.activity?.isCompleted &&
                oldItem.activity?.title == newItem.activity?.title
    }
}