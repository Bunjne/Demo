package whiz.sspark.library.view.widget.class_schedule

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.ScheduleSlot
import whiz.sspark.library.extension.toDP
import java.text.SimpleDateFormat
import java.util.*

class ScheduleRowView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var scheduleTimes = listOf<String>()

    private val defaultSlotPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.primaryColor)
        }
    }

    private val verticalLinePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.textBaseThirdColor)
        }
    }

    private val horizontalLinePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.accentGrayV500)
            strokeWidth = 1f.toDP(context)
        }
    }

    private val textPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.textBasePrimaryColor)
            textSize = 9.toDP(context).toFloat()
            typeface = SSparkLibrary.boldTypeface
            textAlign = Paint.Align.CENTER
        }
    }

    private val slotPaints = arrayListOf<Paint>()

    private var metrics = DisplayMetrics()
    private var rowWidth = 0.0f
    private var rowHeight = 0.0f
    private var columnWidth = 0.0f

    private val multiTextLineSpace = 10.toDP(context)

    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var oneMilliSecondWidth = 0f
    private var scheduleStartTime = 0L

    private var verticalLineCount = 0
    private var title = ""
    private var desiredDurations = listOf<ScheduleSlot>()
    private var isColumnTitleShown = false
    private var isRowTitleShown = false
    private var isUnderlineShown = false

    init {
        try {
            (context as AppCompatActivity).windowManager.defaultDisplay.getMetrics(metrics)
        } catch (exception: Exception) {
            metrics = Resources.getSystem().displayMetrics
        }

        rowWidth = metrics.widthPixels.toFloat()
        rowHeight = textPaint.descent() - textPaint.ascent() + multiTextLineSpace
    }

    fun init(rowTitle: String,
             scheduleTimes: List<String>,
             durations: List<ScheduleSlot>,
             isColumnTitleShown: Boolean,
             isRowTitleShown: Boolean = true,
             isUnderlineShown: Boolean) {
        title = rowTitle
        this.scheduleTimes = scheduleTimes
        desiredDurations = durations
        this.isColumnTitleShown = isColumnTitleShown
        this.isRowTitleShown = isRowTitleShown
        this.isUnderlineShown = isUnderlineShown

        rowHeight = ((textPaint.descent() - textPaint.ascent()) * 2) + multiTextLineSpace

        measure(rowWidth.toInt(), rowHeight.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        rowWidth = (parent as View).width.toFloat()
        columnWidth = rowWidth / (scheduleTimes.size + 1)
        scheduleStartTime = timeFormatter.parse(scheduleTimes[0]).time
        oneMilliSecondWidth = columnWidth / (timeFormatter.parse(scheduleTimes[1]).time - scheduleStartTime)

        verticalLineCount = scheduleTimes.size
        if(title.isEmpty()) {
            columnWidth = rowWidth / (scheduleTimes.size)
            oneMilliSecondWidth = columnWidth / (timeFormatter.parse(scheduleTimes[1]).time - scheduleStartTime)
            verticalLineCount = scheduleTimes.size - 1
        }

        desiredDurations.forEach { duration ->
            val slotColor = duration.color

            slotPaints.add(Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.FILL
                color = try {
                    Color.parseColor(slotColor)
                } catch (exception: Exception) {
                    Color.BLACK
                }
            })
        }

        val startingPositionY = 0f

        if (isColumnTitleShown) {
            if(title.isEmpty()) {
                var startingColumnTitleX = 0f
                scheduleTimes.forEach { time ->
                    canvas.drawText(time, startingColumnTitleX + (columnWidth / 2), rowHeight / 1.5f, textPaint)
                    startingColumnTitleX += columnWidth
                }
            } else {
                canvas.drawText(title, columnWidth / 2, startingPositionY * 1.5f, textPaint)
                var startingColumnTitleX = columnWidth
                scheduleTimes.forEach { time ->
                    canvas.drawText(time, startingColumnTitleX + (columnWidth / 2), rowHeight / 1.5f, textPaint)
                    startingColumnTitleX += columnWidth
                }
            }
        } else {
            var baselinePosition = rowHeight / 2 - textPaint.descent()
            for (text in title.split("\n")) {
                canvas.drawText(text, columnWidth / 2, baselinePosition, textPaint)
                baselinePosition += textPaint.descent() - textPaint.ascent()
            }

            canvas.drawLine(0f, startingPositionY, rowWidth, startingPositionY, horizontalLinePaint)

            var startingVerticalLineX = columnWidth
            for (i in 0 until verticalLineCount) {
                canvas.drawLine(startingVerticalLineX, startingPositionY, startingVerticalLineX, rowHeight, verticalLinePaint)
                startingVerticalLineX += columnWidth
            }

            if (isUnderlineShown) {
                canvas.drawLine(0f, rowHeight - 1, rowWidth, rowHeight - 1, horizontalLinePaint)
            }

            val startingFirstSlotX = if (isRowTitleShown) columnWidth else 0f
            desiredDurations.forEachIndexed { index, duration ->
                val startTime = timeFormatter.parse(duration.startTime)
                val endTime = timeFormatter.parse(duration.endTime)

                val differenceMillisecond = endTime.time - startTime.time
                val startingDrawingX = startingFirstSlotX + (startTime.time - scheduleStartTime) * oneMilliSecondWidth

                val rectF = RectF(startingDrawingX,
                        startingPositionY + (rowHeight / 3f),
                        startingDrawingX + (differenceMillisecond * oneMilliSecondWidth),
                        startingPositionY + (rowHeight / 1.5f))

                canvas.drawRoundRect(rectF, 15f, 15f, slotPaints.getOrElse(index, { defaultSlotPaint }))
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = rowWidth.toInt()
        val desiredHeight = (rowHeight).toInt()

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