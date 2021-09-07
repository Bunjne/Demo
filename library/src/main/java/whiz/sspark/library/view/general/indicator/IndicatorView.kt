package whiz.sspark.library.view.general.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.extension.toDP

class IndicatorView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val background by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.viewBaseFourthColor)
        }
    }

    private val selectedIndicator by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.primaryColor)
        }
    }

    private var count = 0
    private var position = 0f
    private var metrics = DisplayMetrics()
    private var width = 0.0f
    private var defaultCorner = 16f.toDP(context)
    private var startPercent = 0.1f
    private var endPercent = 0.9f
    private var positionAtTop = 0f.toDP(context)
    private var positionAtBottom = 4f.toDP(context)

    init {
        metrics = context.resources.displayMetrics
    }

    fun initCount(count: Int) {
        this.count = count
        width = (20.toDP(context) * count).toFloat()
        invalidate()
    }

    fun getPosition() = position

    fun setSelectIndicator(position: Float) {
        this.position = position
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val columnWidth = width / count
        for (i in 0 until count) {
            canvas.drawRoundRect(RectF(columnWidth * (i + startPercent), positionAtTop, columnWidth * (i + endPercent), positionAtBottom), defaultCorner, defaultCorner, background)
        }

        canvas.drawRoundRect(RectF(columnWidth * (position + startPercent), positionAtTop, columnWidth * (position + endPercent), positionAtBottom), defaultCorner, defaultCorner, selectedIndicator)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = width.toInt()
        val desiredHeight = 4.toDP(context)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> Math.min(widthSize, desiredWidth)
            MeasureSpec.UNSPECIFIED -> desiredWidth
            else -> desiredWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> Math.min(heightSize, desiredHeight)
            MeasureSpec.UNSPECIFIED -> desiredHeight
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }
}