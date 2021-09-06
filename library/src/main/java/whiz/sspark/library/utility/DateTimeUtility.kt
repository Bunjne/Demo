package whiz.sspark.library.utility

import android.content.Context
import whiz.sspark.library.R
import java.text.SimpleDateFormat
import java.util.*

fun Date.toThaiYear() = Calendar.getInstance().apply {
    time = this@toThaiYear
    add(Calendar.YEAR, 543)
}.time

fun getDifferenceDayTimelineValue(sourceDate: Date, targetDate: Date): Int {
    val targetDayNumber = Calendar.getInstance().apply {
        time = targetDate
    }.get(Calendar.DAY_OF_YEAR)

    val sourceDayNumber = Calendar.getInstance().apply {
        time = sourceDate
    }.get(Calendar.DAY_OF_YEAR)

    return sourceDayNumber - targetDayNumber
}

fun getLatestUpdatedDateTime(context: Context, date: Date): String {
    val dateTimeFormat = if (isThaiLanguage()) {
        SimpleDateFormat("d/M/yy HH:mm", Locale.getDefault()).format(date.toThaiYear())
    } else {
        SimpleDateFormat("d/M/yy HH:mm", Locale.getDefault()).format(date)
    }
    return context.resources.getString(R.string.screen_header_subtitle, dateTimeFormat)
}