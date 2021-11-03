package whiz.sspark.library.view.widget.notification_inbox

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewNotificationInboxDateItemBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.toLocalDate
import java.util.*

class NotificationInboxDateItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewNotificationInboxDateItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(date: Date) {
        if (DateUtils.isToday(date.toLocalDate()!!.time)) {
            resources.getString(R.string.general_text_today)
        } else {
            binding.tvDate.text = date.toLocalDate()!!.convertToDateString(
                defaultPattern = DateTimePattern.dayAbbreviateMonthFormatEn,
                dayMonthThPattern = DateTimePattern.dayAbbreviateMonthFormatTh,
                yearThPattern = DateTimePattern.generalYear
            )
        }
    }
}
