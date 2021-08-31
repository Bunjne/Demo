package whiz.sspark.library.extension

import android.text.format.DateFormat
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

fun Date.toTodayAbbreviatedDateFormat() = if (isThaiLanguage()) {
    SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(this.toThaiYear())
} else {
    SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(this)
}

fun Date?.toNormalDate(): String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)

fun Date.toRepresentDay(): String = DateFormat.format("d", this).toString()
fun Date.toRepresentDayName(): String = DateFormat.format("EEE", this).toString()