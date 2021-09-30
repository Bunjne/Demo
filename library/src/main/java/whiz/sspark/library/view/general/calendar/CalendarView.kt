package whiz.sspark.library.view.general.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.CalendarEntry
import whiz.sspark.library.data.enum.CalendarEventType
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.extension.toDP
import java.util.*

class CalendarView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val calendarDayTitleTextSize by lazy {
        resources.getDimension(R.dimen.CalendarDayTitleTextSize)
    }

    private val calendarTextSize by lazy {
        resources.getDimension(R.dimen.CalendarTextSize)
    }

    private val examAmountTextSize by lazy {
        resources.getDimension(R.dimen.ExamAmountTextSize)
    }

    private val regularTypeface by lazy {
        SSparkLibrary.regularTypeface
    }

    private val boldTypeface by lazy {
        SSparkLibrary.boldTypeface
    }

    private val examCalendarBackgroundColor by lazy {
        ContextCompat.getColor(context, R.color.primaryColor)
    }

    private val calendarDayTitleTextPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.textBaseThirdColor)
            style = Paint.Style.FILL
            textSize = calendarDayTitleTextSize
            textAlign = Paint.Align.CENTER
            typeface = boldTypeface
        }
    }

    private val calendarTextPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.textBaseSecondaryColor)
            style = Paint.Style.FILL
            textSize = calendarTextSize
            textAlign = Paint.Align.CENTER
            typeface = regularTypeface
        }
    }

    private val calendarDimTextPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.textBaseThirdColor)
            style = Paint.Style.FILL
            textSize = calendarTextSize
            textAlign = Paint.Align.CENTER
            typeface = regularTypeface
        }
    }

    private val highlightExamAmountStrokePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = examCalendarBackgroundColor
            strokeWidth = 2f.toDP(context)
            style = Paint.Style.STROKE
        }
    }

    private val highlightExamAmountBackgroundPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
    }

    private val highlightExamAmountTextPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            textSize = examAmountTextSize
            typeface = boldTypeface
            textAlign = Paint.Align.CENTER
        }
    }

    private val highlightExamBackgroundPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = examCalendarBackgroundColor
            style = Paint.Style.FILL
        }
    }

    private val highlightExamTextPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            textSize = calendarTextSize
            textAlign = Paint.Align.CENTER
            typeface = regularTypeface
        }
    }

    private val calendarHighlightGeneralTextPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            textSize = calendarTextSize
            textAlign = Paint.Align.CENTER
            typeface = regularTypeface
        }
    }

    private val calendarHighlightTextPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            textSize = resources.getDimension(R.dimen.CalendarTextSize)
            textAlign = Paint.Align.CENTER
            typeface = regularTypeface
        }
    }

    private val selectedCalendar = Calendar.getInstance()
    private var month = 0
    private var year = 0
    private var highlightDays = listOf<CalendarEntry>()
    private var isExamCalendar = false

    private val defaultPadding = 0f
    private val days = resources.getStringArray(R.array.full_day_name)
    private val maxDayNumber = 7
    private val weekDays = listOf(1, 7)
    private var actualWidth = 0.0f
    private var actualHeight = 0.0f
    private var columnWidth = 0.0f
    private var rowHeight = 0.0f
    private var centerTextOffset = 0.0f
    private val highlightRadius = 48f

    private var desiredViewHeight = 0.0f

    private var isExamWeekendSkipped = false

    init {
        val metrics = context.resources.displayMetrics
        actualWidth = metrics.widthPixels.toFloat() - (paddingLeft + paddingRight + defaultPadding + defaultPadding)
        actualHeight = metrics.heightPixels / 3f
        columnWidth = (actualWidth / (days.size + 1))
        rowHeight = (actualHeight / 6f)
        centerTextOffset = columnWidth / 2
    }

    fun init(month: Int,
             year: Int,
             entries: List<CalendarEntry>,
             isExamCalendar: Boolean) {

        this.month = month
        this.year = year
        this.isExamCalendar = isExamCalendar

        highlightDays = entries

        val maxWeekNumber = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
            add(Calendar.DAY_OF_MONTH, -1)
        }.get(Calendar.WEEK_OF_MONTH)

        desiredViewHeight = (rowHeight * maxWeekNumber) + (rowHeight)
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        actualWidth = canvas.width.toFloat()
        columnWidth = (actualWidth / (days.size))
        centerTextOffset = columnWidth / 2

        with(selectedCalendar) {
            set(Calendar.MONTH, month - 1)
            set(Calendar.YEAR, year)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        var startingTitleX = defaultPadding

        days.forEach {
            canvas.drawText(it, startingTitleX + centerTextOffset, rowHeight / 2, calendarDayTitleTextPaint)
            startingTitleX += columnWidth
        }

        val startingPositionY = rowHeight / 2 // The first row is for the abbreviated day of week

        while (isSameMonth()) {
            val weekOfMonth = selectedCalendar.get(Calendar.WEEK_OF_MONTH)
            while (selectedCalendar.get(Calendar.WEEK_OF_MONTH) == weekOfMonth) {
                val dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)
                val dayPositionX = defaultPadding + (columnWidth * (dayOfWeek - 1))
                val dayPositionY = startingPositionY + (rowHeight * weekOfMonth)
                val day = selectedCalendar.get(Calendar.DAY_OF_MONTH)

                drawDay(canvas, day, dayPositionX, dayPositionY, isExamCalendar)
            }
        }
    }

    private fun drawDay(canvas: Canvas, day: Int, x: Float, y: Float, isExamCalendar: Boolean) {
        if (isSameMonth()) {
            val highlightDay = highlightDays.singleOrNull { it.day == day }
            if (isExamCalendar) { // If the exam calendar is being rendered
                if (highlightDay != null) {

                    drawHighlightCircleBackground(canvas, x, y, highlightExamBackgroundPaint)

                    val circleOffsetX = columnWidth / 1.4f
                    val circleOffsetY = columnWidth / 4
                    val circleStartingPointX = x + circleOffsetX

                    canvas.drawCircle(circleStartingPointX,
                        y - circleOffsetY,
                        columnWidth / 10f,
                        highlightExamAmountBackgroundPaint)

                    canvas.drawCircle(circleStartingPointX,
                        y - circleOffsetY,
                        columnWidth / 8f,
                        highlightExamAmountStrokePaint)

                    val examAmountPositionY = (y - (columnWidth / 4)) + highlightExamAmountTextPaint.descent() - 1.toDP(context)

                    canvas.drawText(highlightDay.eventCount.toString(), circleStartingPointX, examAmountPositionY, highlightExamAmountTextPaint)

                    canvas.drawText(day.toString(), x + centerTextOffset, y, highlightExamTextPaint)
                } else {
                    drawPlainDay(canvas, day, x, y)
                }

                selectedCalendar.add(Calendar.DAY_OF_MONTH, 1)

            } else { // If the USPARK Calendar is being rendered
                if (highlightDay != null) {

                    val dayTextColor = if (highlightDay.type == CalendarEventType.GENERAL) calendarHighlightGeneralTextPaint
                    else calendarHighlightTextPaint

                    if (highlightDay.eventCount == 1) { // If the event is not consecutive

                        val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            color = highlightDay.colorCode.toColor(ContextCompat.getColor(context, R.color.viewBaseFourthColor))
                        }
                        drawHighlightCircleBackground(canvas, x, y, backgroundPaint)
                        canvas.drawText(day.toString(), x + centerTextOffset, y, dayTextColor)
                        selectedCalendar.add(Calendar.DAY_OF_MONTH, 1)
                    } else {
                        var totalDays = highlightDay.eventCount

                        drawHighlightCalendarEvent(canvas, highlightDay, x, y)

                        var dayPositionY = y
                        while (totalDays > 0) {
                            val weekOfMonth = selectedCalendar.get(Calendar.WEEK_OF_MONTH)
                            while (selectedCalendar.get(Calendar.WEEK_OF_MONTH) == weekOfMonth && totalDays > 0) {
                                val dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)
                                val dayPositionX = defaultPadding + (columnWidth * (dayOfWeek - 1))
                                val currentDay = selectedCalendar.get(Calendar.DAY_OF_MONTH)

                                if(isWeekEnd() && isExamWeekendSkipped && highlightDay.type == CalendarEventType.EXAM) {
                                    canvas.drawText(currentDay.toString(),
                                        dayPositionX + centerTextOffset,
                                        dayPositionY,
                                        calendarDimTextPaint)
                                } else {
                                    canvas.drawText(currentDay.toString(),
                                        dayPositionX + centerTextOffset,
                                        dayPositionY,
                                        dayTextColor)
                                }

                                totalDays--
                                selectedCalendar.add(Calendar.DAY_OF_MONTH, 1)
                            }
                            dayPositionY += rowHeight
                        }
                    }
                } else {
                    drawPlainDay(canvas, day, x, y)
                    selectedCalendar.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
        } else {
            selectedCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    private fun drawPlainDay(canvas: Canvas, day: Int, x: Float, y: Float) {
        if (isWeekEnd()) {
            canvas.drawText(day.toString(), x + centerTextOffset, y, calendarDimTextPaint)
        } else {
            canvas.drawText(day.toString(), x + centerTextOffset, y, calendarTextPaint)
        }
    }

    private fun drawHighlightCircleBackground(canvas: Canvas, x: Float, y: Float, paint: Paint) {
        canvas.drawCircle(x + centerTextOffset, y - (rowHeight / 8), columnWidth / 4.2f, paint)
    }

    private fun drawHighlightCalendarEvent(canvas: Canvas,
                                           highlightDay: CalendarEntry,
                                           x: Float,
                                           y: Float) {

        val highlightOffsetX = columnWidth / 5.0f
        val highlightOffsetTopY = rowHeight / 2.0f
        val highlightOffsetBottomX = rowHeight / 4.0f

        val currentDayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)
        val eventCount = highlightDay.eventCount
        val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = highlightDay.colorCode.toColor(ContextCompat.getColor(context, R.color.viewBaseFourthColor))
        }

        if (((currentDayOfWeek + eventCount) - 1) > maxDayNumber) {
            val positionEndX = if (highlightDay.type == CalendarEventType.EXAM && isExamWeekendSkipped) {
                (columnWidth * (maxDayNumber - 1)) - (highlightOffsetX / 1f)
            } else {
                (columnWidth * (maxDayNumber)) - (highlightOffsetX / 1f)
            }
            val rectF = RectF(x + highlightOffsetX,
                y - highlightOffsetTopY,
                positionEndX,
                y + highlightOffsetBottomX)
            canvas.drawRoundRect(rectF, highlightRadius, highlightRadius, backgroundPaint)

            var remainingDays = (eventCount - (maxDayNumber - currentDayOfWeek)) - 1

            val startingPositionX = if(highlightDay.type == CalendarEventType.EXAM && isExamWeekendSkipped) columnWidth + defaultPadding else defaultPadding
            var positionY = y + rowHeight

            while (remainingDays > 0) {
                val additionalRectF = if (remainingDays >= maxDayNumber) {
                    RectF(startingPositionX + highlightOffsetX,
                        positionY - highlightOffsetTopY,
                        positionEndX,
                        positionY + highlightOffsetBottomX)
                } else {
                    val desiredPositionEndX = (startingPositionX + (columnWidth * (remainingDays))) - highlightOffsetX
                    RectF(startingPositionX + highlightOffsetX,
                        positionY - highlightOffsetTopY,
                        desiredPositionEndX,
                        positionY + highlightOffsetBottomX)
                }

                canvas.drawRoundRect(additionalRectF, highlightRadius, highlightRadius, backgroundPaint)
                remainingDays -= maxDayNumber
                positionY += rowHeight
            }
        } else {
            val positionEndX = (x + (columnWidth * eventCount)) - (highlightOffsetX / 1.2f)
            val rectF = RectF(x + highlightOffsetX,
                y - highlightOffsetTopY,
                positionEndX,
                y + highlightOffsetBottomX)
            canvas.drawRoundRect(rectF, highlightRadius, highlightRadius, backgroundPaint)
        }
    }

    private fun isSameMonth() = selectedCalendar.get(Calendar.MONTH) == (month - 1)

    private fun isWeekEnd() = weekDays.contains(selectedCalendar.get(Calendar.DAY_OF_WEEK))

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = actualWidth.toInt()
        val desiredHeight = desiredViewHeight.toInt()

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