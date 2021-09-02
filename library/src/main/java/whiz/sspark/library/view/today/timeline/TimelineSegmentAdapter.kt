package whiz.sspark.library.view.today.timeline

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.extension.toDP
import java.util.*

class TimelineSegmentAdapter(private val context: Context,
                             private val items: List<Item>,
                             private val onSegmentClicked: (Date, Int, Int) -> Unit) : RecyclerView.Adapter<TimelineSegmentAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private var selectedPosition = 0

    fun setPosition(position: Int) {
        val lastSelectedPosition = selectedPosition
        notifyItemChanged(lastSelectedPosition, false)
        selectedPosition = position
        notifyItemChanged(position, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TimelineSegmentView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getOrNull(position)

        item?.let {
            (holder.itemView as TimelineSegmentView).apply {
                val isToday = it.differenceDay == 0
                init(item.date!!)

                setOnClickListener {
                    onSegmentClicked(item.date, item.differenceDay!!, position)
                }

                if (selectedPosition == position) {
                    setSelectedView(isToday)
                } else {
                    setUnselectedView(isToday)
                }

                when (position) {
                    0 -> setPadding(12.toDP(context), 0, 0, 0)
                    items.lastIndex -> setPadding(12.toDP(context), 0, 12.toDP(context), 0)
                    else -> setPadding(12.toDP(context), 0, 0, 0)
                }
            }

        }
    }

    data class Item(
        val date: Date? = null,
        val differenceDay: Int? = null
    )
}