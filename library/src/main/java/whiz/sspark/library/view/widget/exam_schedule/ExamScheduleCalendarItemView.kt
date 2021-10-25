package whiz.sspark.library.view.widget.exam_schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.ExamScheduleCalendar
import whiz.sspark.library.databinding.ViewExamScheduleCalendarItemBinding

class ExamScheduleCalendarItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewExamScheduleCalendarItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(examScheduleCalendar: ExamScheduleCalendar) {
        with(examScheduleCalendar) {
            binding.vCalendar.init(
                month = month,
                year = year,
                entries = entries,
                isExamCalendar = isExamCalendar,
            )
        }
    }
}