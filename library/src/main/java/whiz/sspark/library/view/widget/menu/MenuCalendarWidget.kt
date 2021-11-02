package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.CalendarWidgetItem
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewMenuCalendarWidgetBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.toLocalDate

class MenuCalendarWidget: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewMenuCalendarWidgetBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(calendarWidgetInfo: CalendarWidgetItem) {
        binding.ivGradient.show(R.drawable.bg_primary_gradient_0)
        binding.ivArrow.show(R.drawable.ic_arrow_right)

        val activity = if (calendarWidgetInfo.title.isNotBlank()) {
            calendarWidgetInfo.title
        } else {
            resources.getString(R.string.general_no_activity_text)
        }

        binding.tvDay.text = calendarWidgetInfo.date.toLocalDate()!!.convertToDateString(DateTimePattern.fullDayNameFormat)
        binding.tvDate.text = calendarWidgetInfo.date.toLocalDate()!!.convertToDateString(DateTimePattern.singleDayFormat)
        binding.tvActivity.text = activity
    }
}