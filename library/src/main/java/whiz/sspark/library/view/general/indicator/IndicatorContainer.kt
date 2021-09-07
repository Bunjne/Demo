package whiz.sspark.library.view.general.indicator

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import whiz.sspark.library.databinding.ViewIndicatorContainerBinding

class IndicatorContainer: LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewIndicatorContainerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var size = 0
    private var currentPosition = 0f
    private var isScrollByUser = false
    private var targetPosition = 0f
    private var defaultAnimationDuration = 200L

    fun init(size: Int) {
        this.size = size

        if (size <= 1) {
            binding.vIndicator.visibility = View.GONE
        } else {
            binding.vIndicator.initCount(size)
        }
    }

    fun onPageSelected(position: Int) {
        targetPosition = (position % size).toFloat()

        var animation = ScrollIndicatorAnimation().apply {
            init(binding.vIndicator, targetPosition)
            duration = getAnimationDuration()
        }

        binding.vIndicator.startAnimation(animation)
        currentPosition = targetPosition
    }

    fun onPageScrollStateChanged(state: Int) {
        if (state == 0) {
            isScrollByUser = false
        } else if (state == 1) {
            isScrollByUser = true
        }
    }

    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (positionOffset != 0f && isScrollByUser) {
            targetPosition = position + positionOffset
            binding.vIndicator.setSelectIndicator(targetPosition)
            currentPosition = targetPosition
        }
    }

    private fun getAnimationDuration(): Long {
        val positionDiff = currentPosition - targetPosition
        return if (positionDiff == 1f || positionDiff == -1f) {
            defaultAnimationDuration
        } else {
            if (positionDiff >= 0) {
                (defaultAnimationDuration * positionDiff).toLong()
            } else {
                (defaultAnimationDuration * (positionDiff * -1)).toLong()
            }
        }
    }
}