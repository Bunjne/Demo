package whiz.sspark.library.extension

import androidx.core.graphics.ColorUtils
import android.graphics.Color

fun Int.withAlpha(alpha: Int) = ColorUtils.setAlphaComponent(this, alpha)

fun String.toColor(color: Int = Color.BLACK) = try {
    Color.parseColor(this)
} catch (exception: Exception) {
    color
}