package whiz.sspark.library.view.widget.school_record_activity

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.ActivityRecordItem
import whiz.sspark.library.view.widget.base.ItemListTitleView
import java.lang.IndexOutOfBoundsException

class JuniorActivityRecordAdapter(private val context: Context): ListAdapter<JuniorActivityRecordAdapter.Item, RecyclerView.ViewHolder>(JuniorActivityRecordDiffCallback()) {

    companion object {
        private val TITLE_VIEW_TYPE = 1111
        private val STATUS_WITH_DESCRIPTON_VIEW_TYPE = 2222
        private val STATUS_VIEW_TYPE = 3333
        private val TITLE_WITH_DESCRIPTION_VIEW_TYPE = 4444
    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_VIEW_TYPE
        }

        return when {
            item.activityRecordItem?.status != null && item.activityRecordItem.description != null -> STATUS_WITH_DESCRIPTON_VIEW_TYPE
            item.activityRecordItem?.status != null -> STATUS_VIEW_TYPE
            item.activityRecordItem?.description != null -> TITLE_WITH_DESCRIPTION_VIEW_TYPE
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
                StatusWithDescriptionViewHolder(JuniorActivityRecordStatusWithDescriptionItemView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            STATUS_VIEW_TYPE -> {
                StatusViewHolder(JuniorActivityRecordStatusItemView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            TITLE_WITH_DESCRIPTION_VIEW_TYPE -> {
                TitleWithDescriptionViewHolder(JuniorActivityRecordTitleWithDescriptionItemView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            else -> {
                TitleViewHolder(ItemListTitleView(context).apply {
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

            when(viewType) {
                STATUS_WITH_DESCRIPTON_VIEW_TYPE -> {
                    (holder.itemView as? JuniorActivityRecordStatusWithDescriptionItemView)?.apply {
                        init(
                            status = it.activityRecordItem?.status ?: false,
                            title = it.activityRecordItem?.title ?: "",
                            description = it.activityRecordItem?.description ?: ""
                        )
                    }
                }
                STATUS_VIEW_TYPE -> {
                    (holder.itemView as? JuniorActivityRecordStatusItemView)?.apply {
                        init(
                            status = it.activityRecordItem?.status ?: false,
                            title = it.activityRecordItem?.title ?: ""
                        )
                    }
                }
                TITLE_WITH_DESCRIPTION_VIEW_TYPE -> {
                    (holder.itemView as? JuniorActivityRecordTitleWithDescriptionItemView)?.apply {
                        init(
                            title = it.activityRecordItem?.title ?: "",
                            description = it.activityRecordItem?.description ?: ""
                        )
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
        val activityRecordItem: ActivityRecordItem? = null
    )

}

private class JuniorActivityRecordDiffCallback : DiffUtil.ItemCallback<JuniorActivityRecordAdapter.Item>() {
    override fun areItemsTheSame(oldItem: JuniorActivityRecordAdapter.Item, newItem: JuniorActivityRecordAdapter.Item): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: JuniorActivityRecordAdapter.Item, newItem: JuniorActivityRecordAdapter.Item): Boolean {
        return oldItem.title == newItem.title &&
                oldItem.activityRecordItem?.description == newItem.activityRecordItem?.description &&
                oldItem.activityRecordItem?.status == newItem.activityRecordItem?.status &&
                oldItem.activityRecordItem?.title == newItem.activityRecordItem?.title
    }
}