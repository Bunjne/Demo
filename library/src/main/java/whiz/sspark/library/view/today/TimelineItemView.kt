package whiz.sspark.library.view.today

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.TimelineItem
import whiz.sspark.library.data.enum.TimeLineItemType
import whiz.sspark.library.data.enum.TimelineColorStyle
import whiz.sspark.library.databinding.ViewBottomNavigationBarItemBinding
import whiz.sspark.library.databinding.ViewTimelineItemBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.utility.convertDateToTime

class TimelineItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewTimelineItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(timelineItem: TimelineItem, onItemClicked: (String) -> Unit, isLastItem: Boolean = false) {
        binding.tvStartTime.text = convertDateToTime(timelineItem.startTime)
        binding.tvEndTime.text = if (timelineItem.endTime.isNotBlank()) {
            resources.getString(R.string.today_timeline_time, convertDateToTime(timelineItem.endTime))
        } else {
            ""
        }

        if (timelineItem.icon.isNotBlank()) {
            binding.ivIcon.show(timelineItem.icon)
            binding.ivIcon.visibility = View.VISIBLE
        } else {
            binding.ivIcon.visibility = View.GONE
        }

        if (timelineItem.backgroundColor.contains(TimelineColorStyle.PRIMARY.style)) {
            binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.white))
            binding.cvIcon.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primaryColor))
            binding.tvStartTime.setTextColor(ContextCompat.getColor(context, R.color.primaryColor))
            binding.tvEndTime.setTextColor(ContextCompat.getColor(context, R.color.primaryColor))
        } else {
            binding.ivIcon.clearColorFilter()
            binding.cvIcon.setCardBackgroundColor(ContextCompat.getColor(context, R.color.viewBaseSecondaryColor))
            binding.tvStartTime.setTextColor(ContextCompat.getColor(context, R.color.fontPrimary))
            binding.tvEndTime.setTextColor(ContextCompat.getColor(context, R.color.fontPrimary))
        }

        binding.flBody.removeAllViews()
        binding.flBody.setOnClickListener {
            onItemClicked(timelineItem.linkTo)
        }

        if (timelineItem.type != TimeLineItemType.EVENT.type) {
            binding.flBody.addView(TimelineBodyView(context).apply {
                visibility = View.VISIBLE

                if (timelineItem.type == TimeLineItemType.APPOINTMENT.type) {
                    setIsRenderAdvisingAppointmentView(true)
                } else {
                    setIsRenderAdvisingAppointmentView(false)
                }

                init(timelineItem.body, timelineItem.backgroundColor)

                val arrowItems = listOf(
                    TimeLineItemType.REGISTRATION_TIME_SLOT.type,
                    TimeLineItemType.CALENDAR.type,
                    TimeLineItemType.CLASS.type,
                    TimeLineItemType.APPOINTMENT.type
                )

                if (arrowItems.contains(timelineItem.type)) {
                    showArrow()
                } else {
                    hideArrow()
                }

                if (isLastItem) {
                    hideVerticalLine()
                }
            })
        } else {
            binding.flBody.addView(TimelineEventBodyView(context).apply {
                visibility = View.VISIBLE
                init(timelineItem.body, timelineItem.backgroundColor)
                showArrow()

                if (isLastItem) {
                    hideVerticalLine()
                }
            })
        }
    }
}