package whiz.sspark.library.view.widget.class_schedule

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
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

    private val courseNamePaint by lazy {
        TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.naturalV100)
            typeface = SSparkLibrary.boldTypeface
            textAlign = Paint.Align.CENTER
        }
    }

    private val slotPaints = arrayListOf<Paint>()
    private val maxCourseNameTextSize = 10
    private val minCourseNameTextSize = 8

    private var metrics = DisplayMetrics()
    private var rowWidth = 0.0f
    private val rowHeight = 30f.toDP(context)
    private var columnWidth = 0.0f

    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var oneMilliSecondWidth = 0f
    private var scheduleStartTime = 0L

    private var verticalLineCount = 0
    private var title = ""
    private var desiredDurations = listOf<ScheduleSlot>()
    private var isColumnTitleShown = false
    private var isRowTitleShown = false
    private var isUnderlineShown = false
    private var bounds = Rect()

    init {
        try {
            (context as AppCompatActivity).windowManager.defaultDisplay.getMetrics(metrics)
        } catch (exception: Exception) {
            metrics = Resources.getSystem().displayMetrics
        }
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
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        rowWidth = width.toFloat()
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
            if (isRowTitleShown) {
                var baselinePosition = rowHeight / 2 - textPaint.descent()
                for (text in title.split("\n")) {
                    canvas.drawText(text, columnWidth / 2, baselinePosition, textPaint)
                    baselinePosition += textPaint.descent() - textPaint.ascent()
                }
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
                val endingDrawingX = (startingDrawingX + (differenceMillisecond * oneMilliSecondWidth))

                val rectF = RectF(startingDrawingX + 0.5f.toDP(context),
                    7f.toDP(context),
                    endingDrawingX - 0.5f.toDP(context),
                    rowHeight - 5f.toDP(context))

                canvas.drawRoundRect(rectF, 6f.toDP(context), 6f.toDP(context), slotPaints.getOrElse(index, { defaultSlotPaint }))

                val slotWidth = rectF.width() - 4.toDP(context)

                for (textSize in maxCourseNameTextSize downTo minCourseNameTextSize) {
                    courseNamePaint.textSize = textSize.toDP(context).toFloat()
                    courseNamePaint.getTextBounds(duration.courseCode, 0, duration.courseCode.length, bounds)

                    if (slotWidth > bounds.width()) {
                        break
                    }
                }

                var baselineCourseCode = (rowHeight / 2) + courseNamePaint.descent()

                val ellipsizedText = TextUtils.ellipsize(duration.courseCode, courseNamePaint, slotWidth, TextUtils.TruncateAt.END).toString().uppercase()
                canvas.drawText(ellipsizedText, rectF.centerX(), baselineCourseCode, courseNamePaint)
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