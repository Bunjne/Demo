package whiz.sspark.library.view.today.happenings.widget.event

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.Event
import whiz.sspark.library.data.entity.HighlightHeader

class HappeningsWidgetEventAdapter(private val context: Context,
                                   private val items: List<Item>,
                                   private val onEventClicked: (Event) -> Unit,
                                   private val onSeeMoreClicked: (HighlightHeader) -> Unit) : RecyclerView.Adapter<HappeningsWidgetEventAdapter.ViewHolder>() {

    private var latestPosition = 0

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HappeningsWidgetEventView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items.getOrNull(position)?.let {
            (holder.itemView as HappeningsWidgetEventView).apply {
                init(it.header, it.events, latestPosition, onEventClicked, onSeeMoreClicked) { position ->
                    latestPosition = position
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items.isNotEmpty()) {
            1
        } else {
            0
        }
    }

    data class Item(
        val events: List<Event>,
        val header: HighlightHeader
    )

    fun setLatestPosition(position: Int){
        latestPosition = position
    }

}