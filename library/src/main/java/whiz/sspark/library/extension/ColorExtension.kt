package whiz.sspark.library.extension

import android.graphics.Color

fun String.toColor(color: Int = Color.BLACK) = try {
    Color.parseColor(this)
} catch (exception: Exception) {
    color
}