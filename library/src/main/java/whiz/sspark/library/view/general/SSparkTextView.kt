package whiz.sspark.library.view.general

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import whiz.sspark.library.R
import whiz.sspark.library.SSparkLibrary
import java.lang.Exception

class SSparkTextView : AppCompatTextView {
    constructor(context: Context) : super(context) { init(null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(attrs) }

    private fun init(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SSparkTextView)
        try {
            val enum = attributes
        } catch (e: Exception) {

        }
        for (index in 0 until attributes.indexCount) {
            val attribute = attributes.getIndex(index)
            when (attribute) {
                R.styleable.SSparkTextView_ssparkFont -> {
                    val fontTypeValue = attributes.getInt(index, 0)
                    val fontTypeface = when (fontTypeValue) {
                        0 -> if (SSparkLibrary.projectType != null) {
                            SSparkLibrary.boldTypeface
                        } else {
                            ResourcesCompat.getFont(context, R.font.noto_sans_thai_semi_bold)
                        }
                        1 -> if (SSparkLibrary.projectType != null) {
                            SSparkLibrary.boldSerifTypeface
                        } else {
                            ResourcesCompat.getFont(context, R.font.noto_sans_thai_semi_bold)
                        }
                        2 -> if (SSparkLibrary.projectType != null) {
                            SSparkLibrary.regularTypeface
                        } else {
                            ResourcesCompat.getFont(context, R.font.noto_sans_thai_regular)
                        }
                        3 -> if (SSparkLibrary.projectType != null) {
                            SSparkLibrary.regularSerifTypeface
                        } else {
                            ResourcesCompat.getFont(context, R.font.noto_sans_thai_regular)
                        }
                        else ->  if (SSparkLibrary.projectType != null) {
                            SSparkLibrary.regularTypeface
                        } else {
                            ResourcesCompat.getFont(context, R.font.noto_sans_thai_regular)
                        }
                    }

                    typeface = fontTypeface
                }
            }
        }
        attributes.recycle()
    }
}