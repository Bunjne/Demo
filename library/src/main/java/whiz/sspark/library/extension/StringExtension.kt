package whiz.sspark.library.extension

import android.util.Patterns
import whiz.sspark.library.data.static.DateTimePattern.serviceDateFullFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.convertToTime(): String {
    return if (this.isBlank()) {
        ""
    } else {
        val timeStamp = this.split(":")
        if (timeStamp.size < 2) {
            ""
        } else {
            "${timeStamp[0]}:${timeStamp[1]}"
        }
    }
}

fun String.toFirstCharacter() = if (this.isBlank()) {
    ""
} else {
    this.trimStart().firstOrNull()?.toString() ?: ""
}

fun String.isUrlValid() = Patterns.WEB_URL.matcher(this).matches()

fun String?.convertToDate(pattern: String) = if (this.isNullOrBlank()) {
    null
} else {
    SimpleDateFormat(pattern, Locale.getDefault()).parse(this)
}