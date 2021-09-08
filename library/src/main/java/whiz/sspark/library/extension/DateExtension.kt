package whiz.sspark.library.extension

import android.content.Context
import whiz.sspark.library.R
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.utility.isThaiLanguage
import whiz.sspark.library.utility.toThaiYear
import java.text.SimpleDateFormat
import java.util.*

fun Date?.toLocalDate(): Date? {
    if (this == null) {
        return null
    }

    val newFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS", Locale.getDefault())
    val convertedDateString = newFormatter.format(this)
    newFormatter.timeZone = TimeZone.getTimeZone("GMT")
    return newFormatter.parse(convertedDateString)
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
    timePattern = "HH:mm"
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

fun Date.toPostTime(context: Context): String {
    val timeDifferences = Calendar.getInstance().time.time - this.toLocalDate()!!.time
    val timeDifferencesInMinutes = (timeDifferences / (1000 * 60))
    val timeDifferencesInHours = timeDifferencesInMinutes / 60

    return when {
        timeDifferencesInHours > 20 -> {
            val dateMonth = this.toLocalDate()?.convertToDateString(DateTimePattern.dayFullMonthFormatTh)
            val time = this.toLocalDate()?.convertToDateString(DateTimePattern.generalTime)
            context.resources.getString(R.string.general_text_hrs)
            val lastUpdated = StringBuilder()
            lastUpdated.append(dateMonth)
            lastUpdated.append(context.resources.getString(R.string.general_text_at))
            lastUpdated.append(" ")
            lastUpdated.append(time)
            lastUpdated.toString()
        }
        timeDifferencesInHours in 1L..20 -> {
            context.resources.getQuantityString(R.plurals.date_time_hour_ago, timeDifferencesInHours.toInt(), timeDifferencesInHours.toInt())
        }
        timeDifferencesInMinutes in 1L..59 -> {
            context.resources.getQuantityString(R.plurals.date_time_minute_ago, timeDifferencesInMinutes.toInt(), timeDifferencesInMinutes.toInt())
        }
        else -> {
            context.getString(R.string.update_just_now)
        }
    }
}