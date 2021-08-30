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

class ScaleIndicator : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

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
            isAntiAlias = true
        }
    }

    private val backgroundPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.viewBaseThirdColor)
        }
    }

    private val currentScorePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.primaryColor)
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

    private val currentScorePath = Path().apply {
        reset()
    }

    private val defaultHeight = 38.toDP(context)
    private val scorePadding = 2f
    private val verticalLineWidth = 3.toDP(context)
    private val textMarginTop = 2.toDP(context)

    private var progressHeight = 0f
    private var progressWidth = 0f
    private var radius = 0f

    private var titles: List<String> = listOf()
    private var currentScore = 0f
    private var columnWidth = 0
    private var startColor = 0
    private var endColor = 0

    private var rectF = RectF()

    fun init(currentScore: Float,
             titles: List<String>) {
        this.titles = titles

        if (currentScore > titles.size) {
            this.currentScore = titles.size.toFloat()
        } else {
            this.currentScore = currentScore
        }
    }

    fun initGradientColor(startColor: Int, endColor: Int) {
        this.startColor = startColor
        this.endColor = endColor
    }

    private fun generateScaleIndicator() {
        val currentScorePathWidth = columnWidth * currentScore + (verticalLineWidth * currentScore)

        generateGradientColor(currentScorePathWidth)

        val isFullScore = titles.size.toFloat() == currentScore
        val isScoreAlmostFull = currentScorePathWidth > progressWidth - ((scorePadding * 2) + radius)
        val isScoreWidthLessThanRadiusWidth = currentScorePathWidth < radius

        when {
            isFullScore -> drawFullScore()
            isScoreAlmostFull -> drawAlmostFullScore(currentScorePathWidth)
            isScoreWidthLessThanRadiusWidth -> drawPieceOfCircleScore(currentScorePathWidth)
            else -> drawCurrentScore(currentScorePathWidth)
        }
    }

    private fun drawFullScore() {
        currentScorePath.apply {
            reset()
            moveTo(
                progressWidth - radius,
                scorePadding
            )
            arcTo(
                progressWidth - ((radius * 2) + scorePadding),
                2f,
                progressWidth - scorePadding,
                progressHeight - scorePadding,
                270f,
                180f,
                false
            )
            lineTo(
                radius + scorePadding,
                progressHeight - scorePadding
            )
            arcTo(
                scorePadding,
                scorePadding,
                (radius * 2) - (scorePadding * 2),
                progressHeight - scorePadding,
                90f,
                180f,
                false
            )
            close()
        }
    }

    private fun drawAlmostFullScore(currentScorePathWidth: Float) {
        val degree = ((currentScorePathWidth - (progressWidth - (radius + (scorePadding * 2)))) / radius) * 180

        currentScorePath.apply {
            reset()
            moveTo(
                currentScorePathWidth,
                scorePadding
            )
            arcTo(
                progressWidth - ((radius * 2) + scorePadding),
                scorePadding,
                progressWidth - scorePadding,
                progressHeight - scorePadding,
                270f,
                degree / 2,
                false
            )
            arcTo(
                progressWidth - ((radius * 2) + scorePadding),
                scorePadding,
                progressWidth - scorePadding,
                progressHeight - scorePadding,
                90f - (degree / 2),
                degree / 2,
                false
            )
            lineTo(
                radius + scorePadding,
                progressHeight - scorePadding
            )
            arcTo(
                scorePadding,
                scorePadding,
                (radius * 2) - (scorePadding * 2),
                progressHeight - scorePadding,
                90f,
                180f,
                false
            )
            close()
        }
    }

    private fun drawCurrentScore(currentScorePathWidth: Float) {
        currentScorePath.apply {
            reset()
            moveTo(
                currentScorePathWidth,
                scorePadding
            )
            lineTo(
                currentScorePathWidth ,
                progressHeight - scorePadding
            )
            lineTo(
                radius + scorePadding,
                progressHeight - scorePadding
            )
            arcTo(
                scorePadding,
                scorePadding,
                (radius * 2) - (scorePadding * 2),
                progressHeight - scorePadding,
                90f,
                180f,
                false
            )
            close()
        }
    }

    private fun drawPieceOfCircleScore(currentScorePathWidth: Float) {
        val degree = (currentScorePathWidth / radius) * 180
        currentScorePath.apply {
            reset()
            arcTo(
                scorePadding,
                scorePadding,
                (radius * 2) - (scorePadding * 2),
                progressHeight - scorePadding,
                180f - (degree / 2),
                degree,
                false
            )
            close()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            canvas.drawPath(backgroundPath, backgroundPaint)
            canvas.drawPath(currentScorePath, currentScorePaint)

            titles.forEachIndexed { index, s ->

                val position = index + 1
                val rectFLeft = scorePadding + (columnWidth * position) + (verticalLineWidth * index)
                val rectFTop = 0f
                val rectFRight = scorePadding + (columnWidth * position) + (verticalLineWidth * position)
                val rectFBottom = progressHeight

                rectF.set(rectFLeft, rectFTop, rectFRight, rectFBottom)

                if (index != titles.lastIndex) {
                    canvas.drawRect(rectF, verticalLinePaint)
                }

                val ellipsizedText = TextUtils.ellipsize(s, textPaint, columnWidth.toFloat(), TextUtils.TruncateAt.END).toString()

                val startPositionX = scorePadding + (columnWidth / 2) + (verticalLineWidth * index) + (columnWidth * index)
                val startPositionY = progressHeight + textMarginTop - textPaint.ascent()

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

        progressHeight = height.toFloat() - 16.toDP(context) - textMarginTop
        progressWidth = width.toFloat()

        radius = progressHeight / 2

        setMeasuredDimension(width, height)

        notifyHeightChange()
    }

    private fun notifyHeightChange() {
        if (titles.isNotEmpty()) {
            columnWidth = ((progressWidth - ((scorePadding * 2) + (verticalLineWidth * (titles.size - 1)))) / titles.size).toInt()
            generateScaleIndicator()

            invalidate()
        }
    }

    private fun generateGradientColor(currentScorePathWidth: Float) {
        currentScorePaint.apply {
            shader = LinearGradient(0f, 0f, currentScorePathWidth, 0f,
                startColor,
                endColor,
                Shader.TileMode.CLAMP)
        }
    }
}