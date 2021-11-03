package whiz.sspark.library.view.widget.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.databinding.ViewEventListHighlightEventItemBinding

class EventListHighlightEventAdapter(private val events: List<EventList>,
                                     private val onEventClicked: (String, String) -> Unit): RecyclerView.Adapter<EventListHighLightEventViewHolder>() {
    companion object {
        private const val EVENT_ITEM_VIEW = 3333
    }

    override fun getItemViewType(position: Int) = EVENT_ITEM_VIEW

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListHighLightEventViewHolder {
        return EventListHighLightEventViewHolder(ViewEventListHighlightEventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: EventListHighLightEventViewHolder, position: Int) {
        val event = events.getOrNull(position)
        event?.let {
            holder.init(it, onEventClicked)
        }
    }

    override fun getItemCount() = events.size
}