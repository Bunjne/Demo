package whiz.sspark.library.view.general.indicator

import android.view.animation.Animation
import android.view.animation.Transformation

class ScrollIndicatorAnimation : Animation() {

    private lateinit var indicatorView: IndicatorView
    private var oldPosition = 0f
    private var newPosition = 0f

    fun init(indicatorView: IndicatorView, position: Float) {
        this.indicatorView = indicatorView
        this.oldPosition = indicatorView.getPosition()
        this.newPosition = position
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        var position = oldPosition + ((newPosition - oldPosition) * interpolatedTime)

        if (::indicatorView.isInitialized) {
            indicatorView.setSelectIndicator(position)
            indicatorView.requestLayout()
        }
    }
}