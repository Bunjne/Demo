package whiz.sspark.library.view.widget.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.databinding.ViewEventListHighlightEventContainerItemBinding

class EventListHighlightEventContainerAdapter(private val onEventClicked: (String, String) -> Unit): ListAdapter<List<EventList>, EventListHighlightEventContainerViewHolder>(DiffCallback()) {

    companion object {
        private const val HIGHLIGHT_EVENT_ITEM_VIEW = 1111
    }

    private var latestPosition = -1

    override fun getItemViewType(position: Int) = HIGHLIGHT_EVENT_ITEM_VIEW

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListHighlightEventContainerViewHolder {
        return EventListHighlightEventContainerViewHolder(ViewEventListHighlightEventContainerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: EventListHighlightEventContainerViewHolder, position: Int) {
        val events = getItem(position)
        holder.init(
            events = events,
            latestPosition = latestPosition,
            onScrolled = {
                latestPosition = it
            },
            onEventClicked = onEventClicked
        )
    }

    private class DiffCallback : DiffUtil.ItemCallback<List<EventList>>() {
        override fun areItemsTheSame(oldItem: List<EventList>, newItem: List<EventList>): Boolean {
            return oldItem.containsAll(newItem) && newItem.containsAll(oldItem)
        }

        override fun areContentsTheSame(oldItem: List<EventList>, newItem: List<EventList>): Boolean {
            return oldItem.containsAll(newItem) && newItem.containsAll(oldItem)
        }
    }
}