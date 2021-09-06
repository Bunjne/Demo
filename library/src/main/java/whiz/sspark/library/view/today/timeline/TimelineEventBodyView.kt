package whiz.sspark.library.view.today.timeline

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.TimelineItemBody
import whiz.sspark.library.data.enum.TimeLineBodyFontStyle
import whiz.sspark.library.data.enum.TimelineColorStyle
import whiz.sspark.library.databinding.ViewTimelineEventBodyBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.toResourceColor
import whiz.sspark.library.extension.withAlpha

class TimelineEventBodyView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewTimelineEventBodyBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(timelineItemBody: List<TimelineItemBody>, backgroundColor: String) {
        val sortedOrderTimeLineItemBody = timelineItemBody.sortedBy { it.order }

        if (backgroundColor.contains(TimelineColorStyle.PRIMARY.style)) {
            val primaryColor = R.color.primaryColor.toResourceColor(context)
            binding.cvBody.setCardBackgroundColor(primaryColor.withAlpha(45))
        }

        binding.llTimelineContent.removeAllViews()
        sortedOrderTimeLineItemBody.forEach { body ->
            if (body.style == TimeLineBodyFontStyle.QR.style) {
                if (!body.text.isNullOrBlank()) {
//                    val qrCodeBitmap = generateSimpleQRCode(body.text, 92f.toDP(context).toInt())
//                    binding.ivQrCode.setImageBitmap(qrCodeBitmap) TODO wait for zxing migration
                }
            } else {
                initContentView(body)
            }
        }
    }

    private fun initContentView(body: TimelineItemBody) {
        binding.llTimelineContent.addView(TimelineContentView(context).apply {
            init(body)
        })
    }
    fun showArrow() {
        binding.ivArrow.show(R.drawable.ic_arrow_right)
        binding.ivArrow.visibility = View.VISIBLE
    }

    fun hideArrow() {
        binding.ivArrow.visibility = View.GONE
    }

    fun hideVerticalLine() {
        binding.tvVerticalLine.visibility = View.INVISIBLE
    }
}