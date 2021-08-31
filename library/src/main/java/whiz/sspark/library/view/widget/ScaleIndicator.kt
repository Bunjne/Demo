package whiz.sspark.library.view.widget

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.extension.toDP
import kotlin.math.absoluteValue

class ScaleIndicator : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val defaultStartColor by lazy {
        ContextCompat.getColor(context, R.color.primaryStartColor)
    }

    private val defaultEndColor by lazy {
        ContextCompat.getColor(context, R.color.primaryEndColor)
    }

    private val textPaint by lazy {
        TextPaint().apply {
            color = ContextCompat.getColor(context, R.color.textBaseThirdColor)
            textSize = resources.getDimension(R.dimen.ScaleIndicatorTextSize)
            typeface = SSparkLibrary.boldTypeface
            textAlign = Paint.Align.CENTER
        }
    }

    private val verticalLinePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.naturalV100)
            alpha = 128
        }
    }

    private val currentProgressPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.primaryColor)
        }
    }

    private val currentProgressPath = Path().apply {
        reset()
    }

    private val backgroundPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.viewBaseThirdColor)
        }
    }

    private val backgroundPath by lazy {
        Path().apply {
            reset()
            moveTo(
                progressWidth - radius,
                0f
            )
            arcTo(
                progressWidth - (radius * 2),
                0f,
                progressWidth,
                progressHeight,
                270f,
                180f,
                false
            )
            lineTo(
                progressWidth - radius,
                progressHeight
            )
            arcTo(
                0f,
                0f,
                radius * 2f,
                progressHeight,
                90f,
                180f,
                false
            )
            close()
        }
    }

    private val defaultHeight = 38.toDP(context)
    private val progressPadding = 2f
    private val verticalLineWidth = 3.toDP(context)
    private val textMarginTop = 2.toDP(context)
    private val textBoxHeight = 14.toDP(context)

    private var progressHeight = 0f
    private var progressWidth = 0f
    private var radius = 0f
    private var columnWidth = 0

    private var rectF = RectF()

    private var titles: List<String> = listOf()
    private var currentProgress = 0f
    private var startColor: Int? = null
    private var endColor: Int? = null

    fun init(currentProgress: Float,
             titles: List<String>) {
        this.titles = titles

        if (currentProgress > titles.size) {
            this.currentProgress = titles.size.toFloat()
        } else {
            this.currentProgress = currentProgress
        }

        generateCurrentProgress()
    }

    fun initGradientColor(startColor: Int, endColor: Int) {
        this.startColor = startColor
        this.endColor = endColor
    }

    private fun generateCurrentProgress() {
        val currentProgressIndicatorWidth = columnWidth * currentProgress

        generateCurrentProgressBarColor(currentProgressIndicatorWidth)

        val isCompleteProgress = titles.size.toFloat() == currentProgress
        val isAlmostCompleteProgress = currentProgressIndicatorWidth > progressWidth - (radius + (progressPadding * 2))
        val isProgressIndicatorWidthLessThanRadius = currentProgressIndicatorWidth < radius
        val isZero = currentProgress == 0f

        when {
            isCompleteProgress -> drawCompleteProgress()
            isAlmostCompleteProgress -> drawAlmostCompleteProgress(currentProgressIndicatorWidth)
            isProgressIndicatorWidthLessThanRadius -> drawPieceOfCircleProgress(currentProgressIndicatorWidth)
            isZero -> resetProgress()
            else -> drawCurrentProgress(currentProgressIndicatorWidth)
        }

        invalidate()
    }

    private fun generateCurrentProgressBarColor(currentProgressIndicatorWidth: Float) {
        currentProgressPaint.apply {
            shader = LinearGradient(0f,
                0f,
                currentProgressIndicatorWidth,
                0f,
                startColor ?: defaultStartColor,
                endColor ?: defaultEndColor,
                Shader.TileMode.CLAMP)
        }
    }

    private fun drawCompleteProgress() {
        currentProgressPath.apply {
            reset()
            moveTo(
                progressWidth - radius,
                progressPadding
            )
            arcTo(
                progressWidth - ((radius * 2) + progressPadding),
                2f,
                progressWidth - progressPadding,
                progressHeight - progressPadding,
                270f,
                180f,
                false
            )
            lineTo(
                radius + progressPadding,
                progressHeight - progressPadding
            )
            arcTo(
                progressPadding,
                progressPadding,
                (radius * 2) - (progressPadding * 2),
                progressHeight - progressPadding,
                90f,
                180f,
                false
            )
            close()
        }
    }

    private fun drawAlmostCompleteProgress(currentProgressIndicatorWidth: Float) {
        val degree = ((currentProgressIndicatorWidth - (progressWidth - (radius + (progressPadding * 2)))) / radius) * 180
        val startPosition = progressWidth - (radius + (progressPadding * 2))

        currentProgressPath.apply {
            reset()
            moveTo(
                startPosition,
                progressPadding
            )
            arcTo(
                progressWidth - ((radius * 2) + progressPadding),
                progressPadding,
                progressWidth - progressPadding,
                progressHeight - progressPadding,
                270f,
                degree / 2,
                false
            )
            arcTo(
                progressWidth - ((radius * 2) + progressPadding),
                progressPadding,
                progressWidth - progressPadding,
                progressHeight - progressPadding,
                90f - (degree / 2),
                degree / 2,
                false
            )
            lineTo(
                radius + progressPadding,
                progressHeight - progressPadding
            )
            arcTo(
                progressPadding,
                progressPadding,
                (radius * 2) - (progressPadding * 2),
                progressHeight - progressPadding,
                90f,
                180f,
                false
            )
            close()
        }
    }

    private fun drawCurrentProgress(currentProgressIndicatorWidth: Float) {
        currentProgressPath.apply {
            reset()
            moveTo(
                currentProgressIndicatorWidth + progressPadding,
                progressPadding
            )
            lineTo(
                currentProgressIndicatorWidth + progressPadding ,
                progressHeight - progressPadding
            )
            lineTo(
                radius + progressPadding,
                progressHeight - progressPadding
            )
            arcTo(
                progressPadding,
                progressPadding,
                (radius * 2) - (progressPadding * 2),
                progressHeight - progressPadding,
                90f,
                180f,
                false
            )
            close()
        }
    }

    private fun drawPieceOfCircleProgress(currentProgressIndicatorWidth: Float) {
        val degree = (currentProgressIndicatorWidth / radius) * 180
        currentProgressPath.apply {
            reset()
            arcTo(
                progressPadding,
                progressPadding,
                (radius * 2) - (progressPadding * 2),
                progressHeight - progressPadding,
                180f - (degree / 2),
                degree,
                false
            )
            close()
        }
    }

    private fun resetProgress() {
        currentProgressPath.apply {
            reset()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            canvas.drawPath(backgroundPath, backgroundPaint)
            canvas.drawPath(currentProgressPath, currentProgressPaint)

            titles.forEachIndexed { index, s ->
                if (index != titles.lastIndex) {
                    val position = index + 1
                    val rectFLeft = progressPadding + (columnWidth * position) - (verticalLineWidth / 2)
                    val rectFTop = 0f
                    val rectFRight = progressPadding + (columnWidth * position) + (verticalLineWidth / 2)
                    val rectFBottom = progressHeight

                    rectF.set(rectFLeft, rectFTop, rectFRight, rectFBottom)

                    canvas.drawRect(rectF, verticalLinePaint)
                }

                val maxTextBoxWidth = columnWidth.toFloat() - 4.toDP(context)
                val textHeight = textPaint.descent() - textPaint.ascent()

                val ellipsizedText = TextUtils.ellipsize(s, textPaint, maxTextBoxWidth, TextUtils.TruncateAt.END).toString()

                val startPositionX = progressPadding + (columnWidth / 2) + (columnWidth * index)
                val startPositionY = progressHeight + textMarginTop + ((textBoxHeight / 2) + (textPaint.ascent().absoluteValue - (textHeight / 2)))

                canvas.drawText(ellipsizedText, startPositionX, startPositionY, textPaint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = resources.displayMetrics.widthPixels
        val desiredHeight = defaultHeight

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

        progressHeight = height.toFloat() - textBoxHeight - textMarginTop
        progressWidth = width.toFloat()

        radius = progressHeight / 2

        setMeasuredDimension(width, height)

        notifyDimensionChange()
    }

    private fun notifyDimensionChange() {
        if (titles.isNotEmpty()) {
            columnWidth = ((progressWidth - (progressPadding * 2)) / titles.size).toInt()
            generateCurrentProgress()
        }
    }
}