package whiz.sspark.library.view.widget.class_schedule.all_class

import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewClassScheduleAllClassItemBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.toLocalDate
import java.util.*

class ClassScheduleAllClassViewHolder(
    private val binding: ViewClassScheduleAllClassItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun init (title: String,
              startDate: Date,
              endDate: Date) {
        val convertedStartDate = startDate.toLocalDate()?.convertToDateString(
            defaultPattern = DateTimePattern.shortDayAndMonthFormatEn,
            dayMonthThPattern = DateTimePattern.shortDayAndMonthFormatTh,
            yearThPattern = DateTimePattern.twoPositionYear
        )

        val convertedEndDate = endDate.toLocalDate()?.convertToDateString(
            defaultPattern = DateTimePattern.shortDayAndMonthFormatEn,
            dayMonthThPattern = DateTimePattern.shortDayAndMonthFormatTh,
            yearThPattern = DateTimePattern.twoPositionYear
        )

        binding.tvTitle.text = title
        binding.tvTime.text = binding.root.context.getString(R.string.class_schedule_range, convertedStartDate, convertedEndDate)
    }
}