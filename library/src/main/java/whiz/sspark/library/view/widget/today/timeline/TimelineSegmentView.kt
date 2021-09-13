package whiz.sspark.library.view.widget.today.timeline

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewTimelineSegmentBinding
import whiz.sspark.library.extension.*
import java.util.*

class TimelineSegmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewTimelineSegmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(date: Date) {
        binding.tvDate.text = date.convertToDateString(DateTimePattern.dayNameThreePositionFormat)
        binding.tvDay.text = date.convertToDateString(DateTimePattern.singleDayFormat)
    }

    fun setSelectedView(isToday: Boolean) {
        binding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.timelineSelectedDayColor))
        binding.tvDay.setTextColor(ContextCompat.getColor(context, R.color.timelineSelectedDayColor))

        val startColor = R.color.primaryStartColor.toResourceColor(context)
        val endColor = R.color.primaryEndColor.toResourceColor(context)
        val transparentColor = R.color.transparentColor.toResourceColor(context)

        if (isToday) {
            binding.tvSegmentTodayTitle.visibility = View.VISIBLE
            binding.tvSegmentTodayTitle.setTextColor(Color.WHITE)
            binding.mcvContainer.background_Color = ContextCompat.getColor(context, R.color.viewBasePrimaryColor).withAlpha(0xDD)
            binding.cvBackground.background_Gradient_Colors = listOf(startColor, startColor, endColor).toIntArray()

            binding.mcvContainer.shadow_Outer_Area = 0f
            binding.cvBackground.shadow_Outer_Area = 2f
            binding.mcvContainer.stroke_Gradient_Colors = listOf(startColor, endColor).toIntArray()
        } else {
            binding.tvSegmentTodayTitle.visibility = View.INVISIBLE
            binding.mcvContainer.background_Color = ContextCompat.getColor(context, R.color.primaryColor).withAlpha(25)
            binding.cvBackground.background_Gradient_Colors = listOf(transparentColor, transparentColor).toIntArray()

            binding.mcvContainer.shadow_Outer_Area = 2f
            binding.cvBackground.shadow_Outer_Area = 0f
            binding.mcvContainer.stroke_Gradient_Colors = listOf(startColor, startColor, endColor).toIntArray()
        }

        binding.mcvContainer.invalidate()
        binding.cvBackground.invalidate()
    }

    fun setUnselectedView(isToday: Boolean) {
        binding.mcvContainer.background_Color = ContextCompat.getColor(context, R.color.viewBasePrimaryColor)
        binding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.textBaseThirdColor))
        binding.tvDay.setTextColor(ContextCompat.getColor(context, R.color.textBaseThirdColor))

        val viewBaseForthColor = R.color.viewBaseFourthColor.toResourceColor(context)
        val transparentColor = R.color.transparentColor.toResourceColor(context)

        if (isToday) {
            binding.tvSegmentTodayTitle.visibility = View.VISIBLE
            binding.tvSegmentTodayTitle.setTextColor(ContextCompat.getColor(context, R.color.textBaseThirdColor))
            binding.cvBackground.background_Gradient_Colors = listOf(viewBaseForthColor, viewBaseForthColor).toIntArray()

            binding.mcvContainer.shadow_Outer_Area = 0f
            binding.cvBackground.shadow_Outer_Area = 2f
            binding.mcvContainer.stroke_Gradient_Colors = listOf(viewBaseForthColor, viewBaseForthColor).toIntArray()
        } else{
            binding.tvSegmentTodayTitle.visibility = View.INVISIBLE
            binding.cvBackground.background_Gradient_Colors = listOf(transparentColor, transparentColor).toIntArray()
            binding.mcvContainer.stroke_Gradient_Colors = listOf(viewBaseForthColor, viewBaseForthColor).toIntArray()

            binding.mcvContainer.shadow_Outer_Area = 2f
            binding.cvBackground.shadow_Outer_Area = 0f
            binding.mcvContainer.stroke_Gradient_Colors = listOf(viewBaseForthColor, viewBaseForthColor).toIntArray()
        }

        binding.mcvContainer.invalidate()
        binding.cvBackground.invalidate()
    }
}