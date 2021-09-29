package whiz.sspark.library.extension

import android.content.res.Resources
import android.graphics.Rect
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

fun DialogFragment.setFullscreenWidth(horizontalPadding: Int = 0) {
    val displayMetrics = Resources.getSystem().displayMetrics
    val rect = displayMetrics.run { Rect(0, 0, widthPixels, heightPixels) }
    val width = rect.width() - (horizontalPadding * 2)
    dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
}