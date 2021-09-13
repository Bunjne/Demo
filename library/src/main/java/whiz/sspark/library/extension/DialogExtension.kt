package whiz.sspark.library.extension

import android.content.res.Resources
import android.graphics.Rect
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

fun DialogFragment.setMatchParentWidth(horizontalPadding: Int) {
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val width = rect.width() - (horizontalPadding * 2)
    dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
}