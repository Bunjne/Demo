package whiz.sspark.library.view.widget.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.databinding.ViewEventListEventItemBinding

class EventListAdapter(private val onEventClicked: (String, String) -> Unit): ListAdapter<EventList, EventListEventViewHolder>(DiffCallback()) {

    companion object {
        private const val EVENT_ITEM_VIEW = 2222
    }

    override fun getItemViewType(position: Int) = EVENT_ITEM_VIEW

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListEventViewHolder {
        return EventListEventViewHolder(ViewEventListEventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: EventListEventViewHolder, position: Int) {
        val event = getItem(position)
        holder.init(event, onEventClicked)
    }

    private class DiffCallback : DiffUtil.ItemCallback<EventList>() {
        override fun areItemsTheSame(oldItem: EventList, newItem: EventList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EventList, newItem: EventList): Boolean {
            return oldItem == newItem
        }
    }
}