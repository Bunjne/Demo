package whiz.sspark.library.view.general.horizontal_proportion

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.extension.toDP

class HorizontalProportionView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var screenWidth = 0f

    private val valueColorPairs = mutableListOf<Pair<Double, Int>>()
    private val proportionBitmaps = mutableListOf<Bitmap>()

    private val lineHeight by lazy {
        10f.toDP(context)
    }

    private val proportionCornerRadius by lazy {
        8f.toDP(context)
    }

    private val proportionMargin by lazy {
        2f.toDP(context)
    }

    private var proportionStep = 0f

    fun init(valueColorPairs: List<Pair<Double, Int>>) {
        val proportionSize = valueColorPairs.size

        try {
            val summaryMargin = if (valueColorPairs.isNotEmpty()) {
                proportionMargin * (valueColorPairs.size - 1)
            } else {
                0f
            }

            screenWidth = width.toFloat() - summaryMargin
            proportionStep = screenWidth / 100f
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        proportionBitmaps.clear()
        with(this.valueColorPairs) {
            clear()
            addAll(valueColorPairs)
        }

        if (valueColorPairs.isEmpty()) {
            val drawable = getRoundedCornerBackground(ContextCompat.getColor(context, R.color.viewBaseThirdColor))
            val lineWidth = (proportionStep * 100.0).toInt()
            val bitmap = Bitmap.createBitmap(lineWidth, lineHeight.toInt(), Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, lineWidth, lineHeight.toInt())
            drawable.draw(canvas)

            proportionBitmaps.add(bitmap)
        } else {
            valueColorPairs.forEachIndexed { index, valueColor ->
                val drawable = if (valueColorPairs.size == 1) {
                    getRoundedCornerBackground(valueColor.second)
                } else {
                    when (index) {
                        0 -> getLeftCornerBackground(valueColor.second)
                        proportionSize - 1 -> getRightCornerBackground(valueColor.second)
                        else -> getCenterBackground(valueColor.second)
                    }
                }

                val lineWidth = (proportionStep * valueColor.first).toInt()
                val bitmap = Bitmap.createBitmap(lineWidth, lineHeight.toInt(), Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, lineWidth, lineHeight.toInt())
                drawable.draw(canvas)

                proportionBitmaps.add(bitmap)
            }
        }

        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        try {
            val summaryMargin = if (valueColorPairs.isNotEmpty()) {
                proportionMargin * (valueColorPairs.size - 1)
            } else {
                0f
            }

            screenWidth = width.toFloat() - summaryMargin
            proportionStep = screenWidth / 100f
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        var currentX = 0f

        if (valueColorPairs.isEmpty()) {
            val bitmap = proportionBitmaps.getOrNull(0)
            bitmap?.let { bitmap ->
                canvas?.drawBitmap(bitmap, currentX, 0f, null)
            }
        } else {
            valueColorPairs.forEachIndexed { index, valueColor ->
                val bitmap = proportionBitmaps.getOrNull(index)
                bitmap?.let { bitmap ->
                    val endingX = currentX + (valueColor.first.toFloat() * proportionStep)
                    canvas?.drawBitmap(bitmap, currentX, 0f, null)
                    currentX = endingX + proportionMargin
                }
            }
        }
    }

    private fun getLeftCornerBackground(color: Int) = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadii = floatArrayOf(proportionCornerRadius, proportionCornerRadius, 0f, 0f, 0f, 0f, proportionCornerRadius, proportionCornerRadius)
        setColor(color)
    }

    private fun getRoundedCornerBackground(color: Int) = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = proportionCornerRadius
        setColor(color)
    }

    private fun getCenterBackground(color: Int) = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(color)
    }

    private fun getRightCornerBackground(color: Int) = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadii = floatArrayOf(0f, 0f, proportionCornerRadius, proportionCornerRadius, proportionCornerRadius, proportionCornerRadius, 0f, 0f)
        setColor(color)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = screenWidth.toInt()
        val desiredHeight = (lineHeight).toInt()

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