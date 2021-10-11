package whiz.sspark.library.extension

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.data.enum.ItemPosition
import whiz.sspark.library.utility.getItemPositionType
import whiz.sspark.library.utility.getLatestUpdatedDateTime
import java.util.*

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, parent: ViewGroup? = this) {
    LayoutInflater.from(context).inflate(layoutRes, parent)
}

fun View.inflate(@LayoutRes layoutRes: Int, parent: ViewGroup? = null) {
    LayoutInflater.from(context).inflate(layoutRes, parent)
}

fun TextView.showViewStateX(dataWrapper: DataWrapperX<Any>?) {
    if (dataWrapper == null) {
        this.showUpdatingData()
    } else {
        with(dataWrapper) {
            if (dataSource == DataSource.CACHE && isNetworkPreferred == true) {
                this@showViewStateX.showUpdatingData()
            } else if (dataSource == DataSource.CACHE && isNetworkPreferred == false) {
                this@showViewStateX.showLatestUpdated(latestDateTime ?: Date())
            } else if (dataSource == DataSource.NETWORK) {
                if (data != null || statusCode == "204") {
                    this@showViewStateX.showLatestUpdated(latestDateTime ?: Date())
                } else {
                    if (!isCacheExisted) {
                        this@showViewStateX.showUpdateFailed()
                    } else {
                        this@showViewStateX.showLatestUpdated(latestDateTime ?: Date())
                    }
                }
            }
        }
    }
}

fun TextView.showLatestUpdated(dateTime: Date = Date()) {
    this.text = getLatestUpdatedDateTime(context, dateTime)
    this.visibility = View.VISIBLE
}

fun TextView.showUpdateFailed() {
    this.text = this.context.resources.getString(R.string.update_failed)
}

fun TextView.showUpdatingData() {
    this.text = this.context.resources.getString(R.string.update_new_data)
}

fun TextView.showUpdatedJustNow() {
    this.text = this.context.resources.getString(R.string.update_just_now)
}

fun View.toDrawable(context: Context): Drawable {
    return BitmapDrawable(context.resources, this.toBitmap(context))
}

fun View.toBitmap(context: Context): Bitmap {
    val displayMetrics = DisplayMetrics()
    (context as? Activity)?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

    this.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    this.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
    this.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
    this.buildDrawingCache()
    val bitmap = Bitmap.createBitmap(this.measuredWidth, this.measuredHeight, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)
    this.draw(canvas)

    return bitmap
}

fun View.setDarkModeBackground(isNextItemHeader: Boolean, isPreviousItemHeader: Boolean) {
    background = when (getItemPositionType(isNextItemHeader, isPreviousItemHeader)) {
        ItemPosition.SINGLE.position -> {
            ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_single)
        }
        ItemPosition.FIRST.position -> {
            ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_top)
        }
        ItemPosition.LAST.position -> {
            ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_bottom)
        }
        else -> {
            ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_middle)
        }
    }
}

fun Window.setGradientDrawable(resourceId: Int) {
    this.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    this.statusBarColor = Color.TRANSPARENT
    this.setBackgroundDrawable(ContextCompat.getDrawable(this.context, resourceId))
}

fun Window.setGradientDrawable(drawable: Drawable) {
    this.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    this.statusBarColor = Color.TRANSPARENT
    this.setBackgroundDrawable(drawable)
}

fun EditText.showKeyboard() {
    this.requestFocus()
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}