package whiz.sspark.library.view.today.happenings.event

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Event
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.view.event.item.EventSmallItemView

class HappeningsEventSmallItemAdapter (private val context: Context,
                                       private val events: List<Event>,
                                       private val onEventClicked: (Event) -> Unit) : RecyclerView.Adapter<HappeningsEventSmallItemAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(EventSmallItemView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events.getOrNull(position)
        event?.let {
            (holder.itemView as? EventSmallItemView)?.apply {
                init(event, onEventClicked)
                when (position) {
                    0 -> holder.itemView.setPadding(8.toDP(context), 0, 0, 0)
                    events.lastIndex -> holder.itemView.setPadding(12.toDP(context), 0, 8.toDP(context), 0)
                    else -> holder.itemView.setPadding(12.toDP(context), 0, 0, 0)
                }
            }
        }
    }

    override fun getItemCount() = events.count()
}