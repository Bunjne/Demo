package whiz.sspark.library.view.widget.calendar

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.CalendarEntry
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.CenterTextItemView
import java.lang.IndexOutOfBoundsException
import java.util.*

class CalendarAdapter(private val onPreviousMonthClicked: () -> Unit,
                      private val onNextMonthClicked: () -> Unit): ListAdapter<CalendarAdapter.CalendarItem, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val MONTH_SELECTION_VIEW_TYPE = 1111
        const val CALENDAR_VIEW_TYPE = 2222
        const val EVENT_VIEW_TYPE = 3333
        const val NO_EVENT_VIEW_TYPE = 4444
    }

    private var recyclerViewHeight = 0

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            return NO_EVENT_VIEW_TYPE
        }

        return when(item) {
            is CalendarItem.MonthSelection -> MONTH_SELECTION_VIEW_TYPE
            is CalendarItem.Calendar -> CALENDAR_VIEW_TYPE
            is CalendarItem.Event -> EVENT_VIEW_TYPE
            is CalendarItem.NoEvent -> NO_EVENT_VIEW_TYPE
        }
    }

    private class MonthSelectionViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private class EventViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private class NoEventViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        recyclerViewHeight = parent.height

        return when(viewType) {
            CALENDAR_VIEW_TYPE -> CalendarViewHolder(CalendarCalendarItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            EVENT_VIEW_TYPE -> EventViewHolder(CalendarEventItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            MONTH_SELECTION_VIEW_TYPE -> MonthSelectionViewHolder(CalendarMonthSelectionItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            else -> NoEventViewHolder(CenterTextItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val isNextItemHeader = getItemViewType(position + 1) != EVENT_VIEW_TYPE
        val isPreviousItemHeader = getItemViewType(position - 1) != EVENT_VIEW_TYPE

        when(item) {
            is CalendarItem.MonthSelection -> (holder.itemView as? CalendarMonthSelectionItemView)?.init(
                monthSelection = item,
                onPreviousMonthClicked = onPreviousMonthClicked,
                onNextMonthClicked = onNextMonthClicked
            )
            is CalendarItem.Calendar -> (holder.itemView as? CalendarCalendarItemView)?.init(item)
            is CalendarItem.Event -> (holder.itemView as? CalendarEventItemView)?.apply{
                init(item)

                setDarkModeBackground(
                    isNextItemHeader = isNextItemHeader,
                    isPreviousItemHeader = isPreviousItemHeader
                )
            }
            is CalendarItem.NoEvent -> (holder.itemView as? CenterTextItemView)?.apply {
                init(item.title)

                post {
                    val lastItemBottom = bottom
                    val lastItemHeight = height
                    val heightDifference = recyclerViewHeight - lastItemBottom

                    holder.itemView.layoutParams.height = lastItemHeight + heightDifference

                    notifyItemChanged(position)
                }
            }
        }
    }

    sealed class CalendarItem {
        data class Calendar(val month: Int, val year: Int, val entries: List<CalendarEntry>, val isExamCalendar: Boolean): CalendarItem()
        data class Event(val startDate: Date, val endDate: Date?, val color: String, val description: String): CalendarItem()
        data class NoEvent(val title: String): CalendarItem()
        data class MonthSelection(val isShowNextButton: Boolean, val isShowPreviousButton: Boolean, val date: Date): CalendarItem()
    }

    private class DiffCallback : DiffUtil.ItemCallback<CalendarItem>() {
        override fun areItemsTheSame(oldItem: CalendarItem, newItem: CalendarItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CalendarItem, newItem: CalendarItem): Boolean {
            return when {
                oldItem is CalendarItem.Calendar && newItem is CalendarItem.Calendar -> oldItem == newItem
                oldItem is CalendarItem.Event && newItem is CalendarItem.Event -> false
                oldItem is CalendarItem.NoEvent && newItem is CalendarItem.NoEvent -> oldItem == newItem
                else -> false
            }
        }
    }
}