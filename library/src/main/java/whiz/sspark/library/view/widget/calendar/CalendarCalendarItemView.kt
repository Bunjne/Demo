package whiz.sspark.library.view.widget.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.CalendarEntry
import whiz.sspark.library.databinding.ViewCalendarCalendarItemBinding

class CalendarCalendarItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewCalendarCalendarItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(calendar: CalendarAdapter.CalendarItem.Calendar) {
        with(calendar) {
            binding.vCalendar.init(
                month = month,
                year = year,
                entries = entries,
                isExamCalendar = isExamCalendar
            )
        }
    }
}