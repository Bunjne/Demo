package whiz.sspark.library.extension

import android.graphics.Color
import android.util.Patterns

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

fun String.toColor(color: Int = Color.BLACK) = try {
    Color.parseColor(this)
} catch (exception: Exception) {
    color
}

fun String.toFirstCharacter() = if (this.isBlank()) {
    ""
} else {
    this.trimStart().firstOrNull()?.toString() ?: ""
}

fun String.isUrlValid() = Patterns.WEB_URL.matcher(this).matches()