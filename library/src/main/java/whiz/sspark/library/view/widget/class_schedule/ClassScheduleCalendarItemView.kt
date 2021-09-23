package whiz.sspark.library.view.widget.class_schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassScheduleCalendar
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewClassScheduleCalendarItemBinding
import whiz.sspark.library.extension.convertToDateString

class ClassScheduleCalendarItemView: LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassScheduleCalendarItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(classScheduleCalendar: ClassScheduleCalendar) {
        binding.llScheduleRow.removeAllViews()
        binding.llScheduleRow.addView(ScheduleRowView(context).apply {
            init(
                rowTitle = " ",
                scheduleTimes = classScheduleCalendar.scheduleTimes,
                durations = listOf(),
                isColumnTitleShown = true,
                isUnderlineShown = false
            )
        })

        val dayGroup = classScheduleCalendar.slots.groupBy { it.dayNumber }
        val scheduleDays = context.resources.getStringArray(R.array.full_day_name)

        scheduleDays.forEachIndexed { index, day ->
            val dayKey = index + 1
            val scheduleSlots = dayGroup.getOrElse(dayKey) { listOf() }
            val convertedDate = classScheduleCalendar.dates.getOrNull(index)?.convertToDateString(DateTimePattern.shortDayAndMonthNoYearFormatTh) ?: ""
            val dayTitle = resources.getString(R.string.class_schedule_day_with_short_date_title, day, convertedDate)

            binding.llScheduleRow.addView(ScheduleRowView(context).apply {
                if (index < scheduleDays.size - 1) {
                    init(
                        rowTitle = dayTitle,
                        scheduleTimes = classScheduleCalendar.scheduleTimes,
                        durations = scheduleSlots,
                        isColumnTitleShown = false,
                        isUnderlineShown = false
                    )
                } else {
                    init(
                        rowTitle = dayTitle,
                        scheduleTimes = classScheduleCalendar.scheduleTimes,
                        durations = scheduleSlots,
                        isColumnTitleShown = false,
                        isUnderlineShown = true
                    )
                }
            })
        }
    }
}