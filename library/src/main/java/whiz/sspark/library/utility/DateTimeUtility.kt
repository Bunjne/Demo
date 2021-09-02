package whiz.sspark.library.utility

import android.content.Context
import whiz.sspark.library.R
import java.text.SimpleDateFormat
import java.util.*

fun Date.toThaiYear() = Calendar.getInstance().apply {
    time = this@toThaiYear
    add(Calendar.YEAR, 543)
}.time

fun getLatestUpdatedDateTime(context: Context, date: Date): String {
    val dateTimeFormat = if (isThaiLanguage()) {
        SimpleDateFormat("d/M/yy HH:mm", Locale.getDefault()).format(date.toThaiYear())
    } else {
        SimpleDateFormat("d/M/yy HH:mm", Locale.getDefault()).format(date)
    }
    return context.resources.getString(R.string.screen_header_subtitle, dateTimeFormat)
}

/*
defaultPattern is default for EN and will be use for thai format when dayMonthThPattern is blank
dayMonthThPattern is date and month in thai format
yearThPattern is year in thai format
timeThPattern is time in thai format

example
    defaultPattern = "dd/MM/yyyy HH:mm"
    dayMonthPattern = "dd/MM/"
    yearPattern = "yyyy "
    timePatter = "HH:mm"
*/
fun Date.convertToDateString(defaultPattern: String, dayMonthThPattern: String = "", yearThPattern: String = "", timeThPattern: String = ""): String {
    return if (isThaiLanguage()) {
        if (dayMonthThPattern.isBlank()) {
            SimpleDateFormat(defaultPattern, Locale.getDefault()).format(this)
        } else {
            val dateMonth = SimpleDateFormat(dayMonthThPattern, Locale.getDefault()).format(this)
            val year = SimpleDateFormat(yearThPattern, Locale.getDefault()).format(this.toThaiYear()) ?: ""
            val time = SimpleDateFormat(timeThPattern, Locale.getDefault()).format(this) ?: ""

            val datetime = StringBuilder()
            datetime.append(dateMonth)
            datetime.append(year)
            datetime.append(time)
            datetime.toString()
        }
    } else {
        SimpleDateFormat(defaultPattern, Locale.getDefault()).format(this)
    }
}