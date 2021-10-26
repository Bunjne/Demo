package whiz.sspark.library.view.widget.event

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.view.widget.base.ItemListTitleView
import whiz.sspark.library.view.widget.event.item.EventRegisteredEventItemView

class EventRegisteredAdapter(private val context: Context,
                             private val items: List<EventRegisteredAdapterViewType>,
                             private val onEventClicked: (String, String) -> Unit): RecyclerView.Adapter<EventRegisteredAdapter.ViewHolder>() {

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
        item?.let {
            with (holder.itemView) {
                when {
                    this is ItemListTitleView && item is EventRegisteredAdapterViewType.Header -> {
                        init(item.title)

                        if (position != 0) {
                            setPadding(0, 14.toDP(context), 0,4.toDP(context))
                        }
                    }
                    this is EventRegisteredEventItemView && item is EventRegisteredAdapterViewType.Event -> {
                        init(item.event, onEventClicked)
                        setPadding(6.toDP(context), 0, 6.toDP(context),0)
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