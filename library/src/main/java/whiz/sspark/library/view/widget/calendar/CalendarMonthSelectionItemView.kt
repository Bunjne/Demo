package whiz.sspark.library.view.widget.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewCalendarMonthSelectionItemBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.show

class CalendarMonthSelectionItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewCalendarMonthSelectionItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(monthSelection: CalendarAdapter.CalendarItem.MonthSelection,
             onNextMonthClicked: () -> Unit,
             onPreviousMonthClicked: () -> Unit) {
        with(monthSelection) {
            binding.tvMonthYear.text = date.convertToDateString(
                defaultPattern = DateTimePattern.fullMonthYearFormat,
                dayMonthThPattern = DateTimePattern.fullMonthFormatTh,
                yearThPattern = DateTimePattern.generalYear
            )

            if (isShowNextButton) {
                binding.ivNextMonth.show(R.drawable.ic_next_page)
                binding.ivNextMonth.visibility = View.VISIBLE
                binding.ivNextMonth.setOnClickListener {
                    onNextMonthClicked()
                }
            } else {
                binding.ivNextMonth.visibility = View.INVISIBLE
                binding.ivNextMonth.setOnClickListener { }
            }

            if (isShowPreviousButton) {
                binding.ivPreviousMonth.show(R.drawable.ic_previous_page)
                binding.ivPreviousMonth.visibility = View.VISIBLE
                binding.ivPreviousMonth.setOnClickListener {
                    onPreviousMonthClicked()
                }
            } else {
                binding.ivPreviousMonth.visibility = View.INVISIBLE
                binding.ivPreviousMonth.setOnClickListener { }
            }
        }
    }
}