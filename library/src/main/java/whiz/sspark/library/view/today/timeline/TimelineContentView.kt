package whiz.sspark.library.view.today.timeline

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.TimelineItemBody
import whiz.sspark.library.data.enum.TimeLineBodyFontStyle
import whiz.sspark.library.data.enum.TimelineColorStyle
import whiz.sspark.library.databinding.ViewTimelineContentBinding
import whiz.sspark.library.extension.show

class TimelineContentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewTimelineContentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(timelineBody: TimelineItemBody) {
        with (binding.tvTitle) {
            text = timelineBody.text
            setTextSize(TypedValue.COMPLEX_UNIT_SP, getFontSize(timelineBody.style))
            typeface = getFontStyle(timelineBody.style)
            if (timelineBody.color.contains(TimelineColorStyle.PRIMARY.style) && timelineBody.style == TimeLineBodyFontStyle.HEADER.style) {
                setTextColor(ContextCompat.getColor(context, R.color.primaryColor))
            }
        }

        if (timelineBody.icon.isNotBlank()) {
            binding.ivIcon.show(timelineBody.icon)
        } else {
            binding.ivIcon.visibility = View.GONE
        }
    }

    private fun getFontStyle(style: String): Typeface? {
        return when (style) {
            TimeLineBodyFontStyle.HEADER.style,
            TimeLineBodyFontStyle.BODY.style -> SSparkLibrary.boldTypeface
            TimeLineBodyFontStyle.FOOTER.style -> SSparkLibrary.regularTypeface
            else -> SSparkLibrary.regularTypeface
        }
    }

    private fun getFontSize(style: String): Float {
        return when (style) {
            TimeLineBodyFontStyle.HEADER.style -> 16f
            TimeLineBodyFontStyle.BODY.style -> 18f
            TimeLineBodyFontStyle.FOOTER.style -> 12f
            else -> 14f
        }
    }

    fun setIconVisibility(isVisible: Boolean) {
        binding.ivIcon.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun setContentTextColor(color: Int) {
        binding.tvTitle.setTextColor(color)
    }
}