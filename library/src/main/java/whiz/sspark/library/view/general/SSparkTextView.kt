package whiz.sspark.library.view.general

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import whiz.sspark.library.R
import whiz.sspark.library.SSparkLibrary

class SSparkTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { style(attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { style(attrs) }

    private fun style(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SSparkTextView)
        for (index in 0 until attributes.indexCount) {
            val attribute = attributes.getIndex(index)
            when (attribute) {
                R.styleable.SSparkTextView_ssparkFont -> {
                    val fontTypeValue = attributes.getInt(index, 0)
                    val fontTypeface = when (fontTypeValue) {
                        0 -> SSparkLibrary.boldTypeface
                        1 -> SSparkLibrary.boldSerifTypeface
                        2 -> SSparkLibrary.regularTypeface
                        3 -> SSparkLibrary.regularSerifTypeface
                        else -> SSparkLibrary.regularTypeface
                    }

                    typeface = fontTypeface
                }
            }
        }
        attributes.recycle()
    }
}