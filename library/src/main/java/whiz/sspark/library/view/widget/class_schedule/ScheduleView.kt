package whiz.sspark.library.view.widget.class_schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import whiz.sspark.library.R
import whiz.sspark.library.SSparkUI
import whiz.sspark.library.data.entity.ScheduleSlot
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewScheduleBinding
import whiz.sspark.library.extension.convertToDateString
import java.util.*

class ScheduleView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewScheduleBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(slots: List<ScheduleSlot>,
             dates: List<Date>) {
        val scheduleTimes = SSparkUI.scheduleTimeNormal.toList()

        binding.llScheduleRow.removeAllViews()
        binding.llScheduleRow.addView(ScheduleRowView(context).apply {
            init(
                rowTitle = " ",
                scheduleTimes = scheduleTimes,
                durations = arrayOf(),
                isColumnTitleShown = true,
                isUnderlineShown = false
            )
        })

        val dayGroup = slots.groupBy { it.dayNumber }

        val scheduleDays = context.resources.getStringArray(R.array.full_day_name)
        scheduleDays.forEachIndexed { index, day ->
            val dayKey = index + 1
            val times = dayGroup.getOrElse(dayKey) { listOf() }
            val durations = times.map { Triple(it.startTime, it.endTime, it.color) }.toTypedArray()

            val dayTitle = resources.getString(R.string.class_schedule_day_with_short_date_title, day, dates.getOrNull(index)?.convertToDateString(DateTimePattern.shortDayAndMonthFormatEn) ?: "")

            binding.llScheduleRow.addView(ScheduleRowView(context).apply {
                if (index < scheduleDays.size - 1) {
                    init(
                        rowTitle = dayTitle,
                        scheduleTimes = scheduleTimes,
                        durations = durations,
                        isColumnTitleShown = false,
                        isUnderlineShown = false
                    )
                } else {
                    init(
                        rowTitle = dayTitle,
                        scheduleTimes = scheduleTimes,
                        durations = durations,
                        isColumnTitleShown = false,
                        isUnderlineShown = true
                    )
                }
            })
        }
    }
}