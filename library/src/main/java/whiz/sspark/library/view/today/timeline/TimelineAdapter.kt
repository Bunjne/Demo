package whiz.sspark.library.view.today.timeline

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.TimelineItem
import whiz.sspark.library.data.enum.TimelineColorStyle

class TimelineAdapter(private val context: Context,
                      private val items: List<TimelineItem>,
                      private val onItemClicked: (String) -> Unit) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    enum class DateType(val type: Int) {
        PRESENT(0),
        OTHER(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TimelineItemView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        item?.let {
            (holder.itemView as TimelineItemView).apply {
                if (position == itemCount - 1) {
                    init(it, onItemClicked, true)
                } else {
                    init(it, onItemClicked)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val backgroundColor = items.getOrNull(position)?.backgroundColor ?: ""
        return if (backgroundColor.contains(TimelineColorStyle.PRIMARY.style)) {
            DateType.PRESENT.type
        } else {
            DateType.OTHER.type
        }
    }
}