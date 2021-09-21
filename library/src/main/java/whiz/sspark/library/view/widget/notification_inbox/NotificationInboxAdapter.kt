package whiz.sspark.library.view.widget.notification_inbox

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.Inbox
import java.util.*

class NotificationInboxAdapter: ListAdapter<NotificationInboxAdapter.Item, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val DATE_VIEW_TYPE = 1111
        private const val INBOX_VIEW_TYPE = 2222
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return if (item?.inbox != null) {
            INBOX_VIEW_TYPE
        } else {
            DATE_VIEW_TYPE
        }
    }

    class DateViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class InboxViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == INBOX_VIEW_TYPE) {
            InboxViewHolder(NotificationInboxItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
        } else {
            DateViewHolder(NotificationInboxDateItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val viewType = getItemViewType(position)

        when(viewType) {
            INBOX_VIEW_TYPE -> (holder.itemView as? NotificationInboxItemView)?.init(item?.inbox!!)
            DATE_VIEW_TYPE -> (holder.itemView as? NotificationInboxDateItemView)?.init(item?.date!!)
        }
    }

    data class Item(
        val date: Date? = null,
        val inbox: Inbox? = null
    )

    private class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}