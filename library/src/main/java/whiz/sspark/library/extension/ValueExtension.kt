package whiz.sspark.library.extension

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat
import whiz.sspark.library.utility.isThaiLanguage

fun Float.toDP(context: Context) = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics))

fun Int.toDP(context: Context) = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)).toInt()

fun Int.toResourceColor(context: Context) = ContextCompat.getColor(context, this)

fun Int.getPercentage(percentage: Int) = this * (percentage/100f)

fun Float.getPercentage(percentage: Int) = this * (percentage/100)

fun Int.toLocalizedYear() = if (isThaiLanguage()) {
    this + 543
} else {
    this
}

fun Long.toLocalizedYear() = if (isThaiLanguage()) {
    this + 543
} else {
    this
}