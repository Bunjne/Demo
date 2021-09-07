package whiz.sspark.library.extension

import androidx.core.graphics.ColorUtils

fun Int.withAlpha(alpha: Int) = ColorUtils.setAlphaComponent(this, alpha)