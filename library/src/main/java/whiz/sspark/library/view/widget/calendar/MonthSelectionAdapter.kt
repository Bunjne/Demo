package whiz.sspark.library.view.widget.calendar

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.MonthSelection

class MonthSelectionAdapter(private val onPreviousMonthClicked: () -> Unit,
                            private val onNextMonthClicked: () -> Unit): ListAdapter<MonthSelection, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val MONTH_SELECTION_VIEW_TYPE = 1111
    }

    override fun getItemViewType(position: Int): Int {
        return MONTH_SELECTION_VIEW_TYPE
    }

    private class MonthSelectionViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MonthSelectionViewHolder(CalendarMonthSelectionItemView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        (holder.itemView as? CalendarMonthSelectionItemView)?.init(
            monthSelection = item,
            onNextMonthClicked = onNextMonthClicked,
            onPreviousMonthClicked = onPreviousMonthClicked
        )
    }

    private class DiffCallback : DiffUtil.ItemCallback<MonthSelection>() {
        override fun areItemsTheSame(oldItem: MonthSelection, newItem: MonthSelection): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MonthSelection, newItem: MonthSelection): Boolean {
            return oldItem == newItem
        }
    }
}