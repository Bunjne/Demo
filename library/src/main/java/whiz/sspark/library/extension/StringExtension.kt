package whiz.sspark.library.extension

import android.util.Patterns
import whiz.sspark.library.utility.isThaiLanguage
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

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

fun String.getFirstConsonant(): String {
    return if (isThaiLanguage()) {
        val pattern = Pattern.compile("[ก-ฮ]")
        for (i in this) {
            if (pattern.matcher(i.toString()).matches()){
                return i.toString()
            }
        }
         ""
    } else {
        this.trimStart().firstOrNull()?.toString() ?: ""
    }
}

fun String.isUrlValid() = Patterns.WEB_URL.matcher(this).matches()

fun String?.convertToDate(pattern: String) = if (this.isNullOrBlank()) {
    null
} else {
    SimpleDateFormat(pattern, Locale.getDefault()).parse(this)
}