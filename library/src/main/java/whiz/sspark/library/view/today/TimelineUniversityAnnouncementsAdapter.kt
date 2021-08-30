package whiz.sspark.library.view.today

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.extension.toDP

class TimelineUniversityAnnouncementsAdapter(private val context: Context,
                                             private val items: List<String>) : RecyclerView.Adapter<TimelineUniversityAnnouncementsAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TimelineUniversityAnnouncementsView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        })
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        item?.let {
            (holder.itemView as TimelineUniversityAnnouncementsView).apply {
                init(it)
                setPadding(12.toDP(context), 12.toDP(context), 12.toDP(context), 12.toDP(context))
            }
        }
    }
}