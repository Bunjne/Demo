package whiz.sspark.library.view.widget.class_schedule

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.WeekSelection

class WeekSelectionAdapter(private val onPreviousWeekClicked: () -> Unit,
                           private val onNextWeekClicked: () -> Unit): ListAdapter<WeekSelection, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val WEEK_SELECTION_VIEW_TYPE = 1111
    }

    override fun getItemViewType(position: Int): Int {
        return WEEK_SELECTION_VIEW_TYPE
    }

    private class WeekSelectionViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WeekSelectionViewHolder(ClassScheduleWeekSelectionItemView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        (holder.itemView as? ClassScheduleWeekSelectionItemView)?.init(
            weekSelection = item,
            onPreviousWeekClicked = onPreviousWeekClicked,
            onNextWeekClicked = onNextWeekClicked
        )
    }

    private class DiffCallback : DiffUtil.ItemCallback<WeekSelection>() {
        override fun areItemsTheSame(oldItem: WeekSelection, newItem: WeekSelection): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeekSelection, newItem: WeekSelection): Boolean {
            return oldItem == newItem
        }
    }
}