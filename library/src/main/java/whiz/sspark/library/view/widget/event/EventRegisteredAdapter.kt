package whiz.sspark.library.view.widget.event

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.view.widget.base.ItemListTitleView
import whiz.sspark.library.view.widget.event.item.EventRegisteredEventItemView

class EventRegisteredAdapter(private val context: Context,
                             private val items: List<EventRegisteredAdapterViewType>): RecyclerView.Adapter<EventRegisteredAdapter.ViewHolder>() {

    companion object {
        const val TITLE_TYPE = 1
        const val EVENT_TYPE = 2
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

        return when (viewType) {
            TITLE_TYPE -> ViewHolder(ItemListTitleView(context).apply {
                this.layoutParams = layoutParams
            })
            else -> ViewHolder(EventRegisteredEventItemView(context).apply {
                this.layoutParams = layoutParams
            })
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        val isNextItemTitle = getItemViewType(position + 1) == TITLE_TYPE
        val isPreviousItemTitle = getItemViewType(position - 1) == TITLE_TYPE

        item?.let {
            with (holder.itemView) {
                when {
                    this is ItemListTitleView && item is EventRegisteredAdapterViewType.Header -> {
                        init(item.title)
                    }
                    this is EventRegisteredEventItemView && item is EventRegisteredAdapterViewType.Event -> {
                        init(item.event)
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