package whiz.sspark.library.view.today

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewTimelineSegmentBinding
import whiz.sspark.library.extension.toRepresentDay
import whiz.sspark.library.extension.toRepresentDayName
import java.util.*

class TimelineSegmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewTimelineSegmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(date: Date) {
        binding.tvDate.text = date.toRepresentDayName()
        binding.tvDay.text = date.toRepresentDay()
    }

    fun setSelectedView(isToday: Boolean) {
        binding.mcvContainer.strokeColor = ContextCompat.getColor(context, R.color.primaryColor)
        binding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.timelineSelectedDayColor))
        binding.tvDay.setTextColor(ContextCompat.getColor(context, R.color.timelineSelectedDayColor))

        if (isToday) {
            binding.tvSegmentTodayTitle.visibility = View.VISIBLE
            binding.tvSegmentTodayTitle.setTextColor(Color.WHITE)
            binding.mcvContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.todaySegmentPrimaryColor))
            binding.cvBackground.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primaryColor))
        } else {
            binding.tvSegmentTodayTitle.visibility = View.INVISIBLE
            binding.mcvContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.selectedSegmentPrimaryColor))
            binding.cvBackground.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparentColor))
        }
    }

    fun setUnselectedView(isToday: Boolean) {
        binding.mcvContainer.strokeColor = ContextCompat.getColor(context, R.color.viewBaseThirdColor)
        binding.mcvContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.appBackgroundColor))
        binding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.textBaseThirdColor))
        binding.tvDay.setTextColor(ContextCompat.getColor(context, R.color.textBaseThirdColor))

        if (isToday) {
            binding.tvSegmentTodayTitle.visibility = View.VISIBLE
            binding.tvSegmentTodayTitle.setTextColor(ContextCompat.getColor(context, R.color.textBaseThirdColor))
            binding.cvBackground.setCardBackgroundColor(ContextCompat.getColor(context, R.color.viewBaseThirdColor))
        } else{
            binding.tvSegmentTodayTitle.visibility = View.INVISIBLE
            binding.cvBackground.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparentColor))
        }
    }
}