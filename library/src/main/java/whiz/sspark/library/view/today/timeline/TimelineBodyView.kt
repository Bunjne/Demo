package whiz.sspark.library.view.today.timeline

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.TimelineItemBody
import whiz.sspark.library.data.enum.Gender
import whiz.sspark.library.data.enum.TimelineColorStyle
import whiz.sspark.library.databinding.ViewTimelineBodyBinding
import whiz.sspark.library.extension.*

class TimelineBodyView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var isRenderAdvisingAppointmentView = false

    private val binding by lazy {
        ViewTimelineBodyBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(timelineItemBody: List<TimelineItemBody>, backgroundColor: String) {
        val sortedOrderTimeLineItemBody = timelineItemBody.sortedBy { it.order }

        if (backgroundColor.contains(TimelineColorStyle.PRIMARY.style)) {
            val primaryColor = R.color.primaryColor.toResourceColor(context)
            binding.cvBody.setCardBackgroundColor(primaryColor.withAlpha(12))

            setPadding(0.toDP(context), 2.toDP(context), 0.toDP(context), 2.toDP(context))
        }

        binding.llTimelineContent.removeAllViews()
        sortedOrderTimeLineItemBody.forEach { body ->
            if (isRenderAdvisingAppointmentView && body.icon.isNotBlank()) {
                binding.ivProfile.showUserProfileCircle(body.icon, Gender.NOTSPECIFY.type)
            }

            initContentView(body)
        }
    }

    private fun initContentView(body: TimelineItemBody) {
        val contentCount = binding.llTimelineContent.childCount
        binding.llTimelineContent.addView(TimelineContentView(context).apply {
            setPadding(0.toDP(context), 0.toDP(context), 0.toDP(context), 0.toDP(context))

            when {
                contentCount == 2 -> {
                    setPadding(0.toDP(context), 8.toDP(context), 0.toDP(context), 8.toDP(context))
                    setContentTextColor(ContextCompat.getColor(context, R.color.textBasePrimaryColor))
                }
                contentCount == 1 && isRenderAdvisingAppointmentView -> setContentTextColor(ContextCompat.getColor(context, R.color.textBaseThirdColor))
                else -> setContentTextColor(ContextCompat.getColor(context, R.color.textBaseSecondaryColor))
            }

            init(body)

            if (isRenderAdvisingAppointmentView && body.icon.isNotBlank()) {
                setIconVisibility(false)
            }
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

    fun setIsRenderAdvisingAppointmentView(isRender: Boolean) {
        isRenderAdvisingAppointmentView = isRender

        if (!isRender) {
            binding.ivProfile.visibility = View.GONE
        } else {
            binding.ivProfile.visibility = View.VISIBLE
        }
    }
}