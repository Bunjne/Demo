package whiz.sspark.library.view.widget.event

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.databinding.ViewEventListEventItemBinding
import whiz.sspark.library.databinding.ViewItemListTitleBinding
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.view.widget.base.ItemListTitleView
import whiz.sspark.library.view.widget.base.ItemListTitleViewHolder

class EventRegisteredAdapter(private val context: Context,
                             private val items: List<EventRegisteredAdapterViewType>,
                             private val onEventClicked: (String, String) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TITLE_TYPE = 1
        const val EVENT_TYPE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TITLE_TYPE -> ItemListTitleViewHolder(ViewItemListTitleBinding.inflate(LayoutInflater.from(context), parent, false))
            else -> EventListEventViewHolder(ViewEventListEventItemBinding.inflate(LayoutInflater.from(context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        item?.let {
            with (holder) {
                when {
                    this is ItemListTitleViewHolder && item is EventRegisteredAdapterViewType.Header -> {
                        init(item.title)

                        if (position != 0) {
                            itemView.setPadding(0, 6.toDP(context), 0, 4.toDP(context))
                        }
                    }
                    this is EventListEventViewHolder && item is EventRegisteredAdapterViewType.Event -> {
                        init(item.event, onEventClicked)

                        if (position == 0) {
                            itemView.setPadding(6.toDP(context), 4.toDP(context), 6.toDP(context),0)
                        } else {
                            itemView.setPadding(6.toDP(context), 0, 6.toDP(context),0)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items.getOrNull(position)) {
            is EventRegisteredAdapterViewType.Header -> TITLE_TYPE
            else -> EVENT_TYPE
        }
    }

    sealed class EventRegisteredAdapterViewType {
        data class Header(val title: String): EventRegisteredAdapterViewType()
        data class Event(val event: EventList): EventRegisteredAdapterViewType()
    }
}