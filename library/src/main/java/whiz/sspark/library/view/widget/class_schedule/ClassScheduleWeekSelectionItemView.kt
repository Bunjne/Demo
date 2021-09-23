package whiz.sspark.library.view.widget.class_schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.WeekSelection
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewClassScheduleWeekSelectionItemBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.show

class ClassScheduleWeekSelectionItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassScheduleWeekSelectionItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(weekSelection: WeekSelection,
             onPreviousWeekClick: () -> Unit,
             onNextWeekClick: () -> Unit) {
        binding.ivPreviousPage.show(R.drawable.ic_previous_page)
        binding.ivNextPage.show(R.drawable.ic_next_page)

        val convertedStartDate = weekSelection.startDate.convertToDateString(
            defaultPattern = DateTimePattern.shortDayAndMonthFormatEn,
            dayMonthThPattern = DateTimePattern.shortDayAndMonthFormatTh,
            yearThPattern = DateTimePattern.twoPositionYear
        )

        val convertedEndDate = weekSelection.endDate.convertToDateString(
            defaultPattern = DateTimePattern.shortDayAndMonthFormatEn,
            dayMonthThPattern = DateTimePattern.shortDayAndMonthFormatTh,
            yearThPattern = DateTimePattern.twoPositionYear
        )

        binding.tvWeekRange.text = resources.getString(R.string.class_schedule_range, convertedStartDate, convertedEndDate)

        if (weekSelection.isShowNextPageButton) {
            binding.ivNextPage.visibility = View.VISIBLE
            binding.ivNextPage.setOnClickListener {
                onNextWeekClick()
            }
        } else {
            binding.ivNextPage.visibility = View.INVISIBLE
        }

        if (weekSelection.isShowPreviousPageButton) {
            binding.ivPreviousPage.visibility = View.VISIBLE
            binding.ivPreviousPage.setOnClickListener {
                onPreviousWeekClick()
            }
        } else {
            binding.ivPreviousPage.visibility = View.INVISIBLE
        }
    }
}