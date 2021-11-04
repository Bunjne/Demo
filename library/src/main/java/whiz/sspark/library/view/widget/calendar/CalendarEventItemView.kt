package whiz.sspark.library.view.widget.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewCalendarEventItemBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.toCalendar
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.extension.toLocalDate
import java.util.*

class CalendarEventItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewCalendarEventItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(event: CalendarAdapter.CalendarItem.Event) {
        val startEventMonth = event.startDate.toLocalDate()?.toCalendar(true)?.get(Calendar.MONTH)
        val endEventMonth = event.endDate.toLocalDate()?.toCalendar(true)?.get(Calendar.MONTH)

        with(event) {
            binding.cvVerticalBar.setCardBackgroundColor(color.toColor(ContextCompat.getColor(context, R.color.viewBaseFourthColor)))
            binding.tvDescription.text = description

            if (endDate != null) {
                if (startEventMonth == endEventMonth) {
                    binding.tvStartDate.text = startDate.toLocalDate()!!.convertToDateString(DateTimePattern.singleDayFormat)
                    binding.tvEndDate.text = resources.getString(R.string.calendar_end_date, endDate.toLocalDate()!!.convertToDateString(DateTimePattern.singleDayFormat))
                    binding.tvEndDate.visibility = View.VISIBLE
                } else {
                    binding.tvStartDate.text = startDate.toLocalDate()!!.convertToDateString(DateTimePattern.dayFullMonthFormatTh)
                    binding.tvEndDate.text = resources.getString(R.string.calendar_end_date, endDate.toLocalDate()!!.convertToDateString(DateTimePattern.dayFullMonthFormatTh))
                    binding.tvEndDate.visibility = View.VISIBLE
                }
            } else {
                binding.tvStartDate.text = startDate.toLocalDate()!!.convertToDateString(DateTimePattern.singleDayFormat)
                binding.tvEndDate.visibility = View.GONE
            }
        }
    }
}