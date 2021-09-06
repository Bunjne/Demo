package whiz.sspark.library.extension

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat

fun Float.toDP(context: Context) = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics))

fun Int.toDP(context: Context) = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)).toInt()

fun Int.toResourceColor(context: Context) = ContextCompat.getColor(context, this)

fun Int.toPercentile(percentage: Int) = this * (percentage/100f)

fun Float.toPercentile(percentage: Int) = this * (percentage/100)