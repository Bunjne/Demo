package whiz.sspark.library.view.general.data_chart_view

import android.content.Context
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.customview.view.AbsSavedState
import whiz.sspark.library.R
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.CourseGroupGrade
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.extension.getPercentage
import kotlin.math.cos
import kotlin.math.sin

class RadarChartView: View {
    constructor(context: Context): super(context) { setupStyleable(context, null) }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) { setupStyleable(context, attrs) }
    constructor(context: Context, attrs: AttributeSet, defStylesRes: Int): super(context, attrs, defStylesRes) { setupStyleable(context, attrs) }

    private val DEFAULT_RADAR_BACKGROUND_COLOR = ContextCompat.getColor(context, R.color.viewBaseThirdColor)
    private val DEFAULT_CATEGORY_TEXT_COLOR = ContextCompat.getColor(context, R.color.textBaseThirdColor)
    private val DEFAULT_RADAR_COLOR = ContextCompat.getColor(context, R.color.primaryColor)
    private val DEFAULT_INNER_LINE_COLOR =  ContextCompat.getColor(context, R.color.naturalV100)

    private var courseGroupGrades = mutableListOf<CourseGroupGrade>()
    private var isDrawText: Boolean = false

    var radarBackgroundColor = DEFAULT_RADAR_BACKGROUND_COLOR
        set(value) {
            field = value
            radarBackgroundPaint.color = value
            invalidate()
        }

    var radarColor = DEFAULT_RADAR_COLOR
        set(value) {
            field = value
            radarPaint.color = value
            invalidate()
        }

    var radarInnerLineColor = DEFAULT_INNER_LINE_COLOR
        set(value) {
            field = value
            innerLinePaint.color = value
            invalidate()
        }

    var numberOfRing = DEFAULT_NUMBER_OF_RING
        set(value) {
            field = value
            invalidate()
        }

    var numberOfCategoryJoints = DEFAULT_CATEGORY_JOINTS_NUMBER
        set(value) {
            field = value
            invalidate()
        }

    var innerLineWidth = DEFAULT_INNER_LINE_WIDTH
        set(value) {
            field = value
            innerLinePaint.strokeWidth = value
            invalidate()
        }

    var textColor = DEFAULT_CATEGORY_TEXT_COLOR
        set(value) {
            field = value
            leftTextPaint.color = value
            rightTextPaint.color = value
            invalidate()
        }

    private var textTypeface = DEFAULT_TEXT_TYPEFACE
        set(value) {
            field = value
            leftTextPaint.typeface = value
            rightTextPaint.typeface = value
            invalidate()
        }

    companion object {
        const val DEFAULT_NUMBER_OF_RING = 4
        const val DEFAULT_INNER_LINE_WIDTH = 4f
        const val DEFAULT_CATEGORY_JOINTS_NUMBER = 8
        val DEFAULT_TEXT_TYPEFACE = SSparkLibrary.regularTypeface
    }

    private var radarBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = radarBackgroundColor
        style = Paint.Style.FILL
    }

    private var innerLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = radarInnerLineColor
        style = Paint.Style.STROKE
        strokeWidth = innerLineWidth
    }

    private var categoryCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var leftTextPaint = TextPaint(TextPaint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        textAlign = Paint.Align.LEFT
        typeface = textTypeface
    }

    private var rightTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        textAlign = Paint.Align.RIGHT
        typeface = textTypeface
    }

    private var radarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = radarColor
        style = Paint.Style.FILL
    }

    private var radarPath = Path()
    private var actualWidth = 0f
    private var desiredViewHeight = 0f
    private var screenWidth = 0f
    private var categoryCirclePadding = 0f
    private var textPadding = 0f
    private val margin by lazy { 8f.toDP(context) }

    init {
        val metrics = context.resources.displayMetrics
        actualWidth = metrics.widthPixels.toFloat() - (paddingStart + paddingEnd + margin + margin)
        desiredViewHeight = 300f.toDP(context)
        screenWidth = metrics.widthPixels.toFloat()
    }

    fun init(courseGroupGrades: List<CourseGroupGrade>,
             isDrawText: Boolean,
             numberOfCategory: Int){
        with(this.courseGroupGrades) {
            clear()
            addAll(courseGroupGrades)
        }
        numberOfCategoryJoints = numberOfCategory
        this.isDrawText = isDrawText
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {
        with(canvas) {
            val defaultPadding = width.getPercentage(15)
            val backgroundRadiusOffset = width / 2 - categoryCirclePadding - textPadding - defaultPadding
            val categoryCircleRadius = backgroundRadiusOffset / 12
            val middleXOffset = width / 2f
            val middleYOffset = height / 2f

            leftTextPaint.textSize = (categoryCircleRadius * 1.8).toFloat()
            rightTextPaint.textSize = (categoryCircleRadius * 1.8).toFloat()

            for (ring in 0 until numberOfRing) {
                val backgroundInnerRadiusOffset = backgroundRadiusOffset - (backgroundRadiusOffset * (0.25f * ring))
                if(ring == 0) {
                    drawRadarBackground(
                        canvas = this,
                        x = middleXOffset,
                        y = middleYOffset,
                        radius = backgroundRadiusOffset
                    )
                } else {
                    drawInnerLine(
                        canvas = this,
                        x = middleXOffset,
                        y = middleYOffset,
                        radius = backgroundInnerRadiusOffset
                    )
                }
            }

            if(courseGroupGrades.isNotEmpty()) {
                var round = 0
                var angle = 360 / numberOfCategoryJoints
                for (i in 0 until 360 step angle) {
                    if (round < courseGroupGrades.size) {
                        val courseGroupGrade = courseGroupGrades[round]
                        val dispatchAngle = i - 90
                        drawInnerJoints(
                            canvas = this,
                            x = middleXOffset,
                            y = middleYOffset,
                            radius = backgroundRadiusOffset,
                            angleOfJoint = dispatchAngle
                        )

                        drawCategories(
                            canvas = this,
                            x = middleXOffset,
                            y = middleYOffset,
                            radius = backgroundRadiusOffset,
                            angleOfJoint = dispatchAngle,
                            categoryCirclePadding = categoryCirclePadding,
                            textPadding = textPadding,
                            text = courseGroupGrade.name,
                            startColor = courseGroupGrade.startColor,
                            endColor = courseGroupGrade.endColor,
                            categoryCircleRadius = categoryCircleRadius
                        )
                    }
                    round += 1
                }

                drawRadar(
                    canvas = this,
                    x = middleXOffset,
                    y = middleYOffset,
                    radius = backgroundRadiusOffset
                )
            }
        }
    }

    private fun drawRadarBackground(canvas: Canvas, x: Float, y: Float, radius: Float) {
        canvas.drawCircle(
            x,
            y,
            radius,
            radarBackgroundPaint
        )
    }

    private fun drawInnerLine(canvas: Canvas, x: Float, y: Float, radius: Float) {
        canvas.drawCircle(
            x,
            y,
            radius,
            innerLinePaint
        )
    }

    private fun drawInnerJoints(canvas: Canvas, x: Float, y: Float, radius: Float, angleOfJoint: Int) {
        val destX: Float = x + (cos(Math.toRadians(angleOfJoint.toDouble())) * radius).toFloat()
        val destY: Float = y + (sin(Math.toRadians(angleOfJoint.toDouble())) * radius).toFloat()
        canvas.drawLine(x, y, destX, destY, innerLinePaint)
    }

    private fun drawCategoryCircle(canvas: Canvas,
                                   x: Float,
                                   y: Float,
                                   categoryCircleRadius: Float,
                                   startColor: Int,
                                   endColor: Int) {
        categoryCirclePaint.shader = LinearGradient(x - categoryCircleRadius, y, x + categoryCircleRadius, y, startColor, endColor, Shader.TileMode.CLAMP)
        canvas.drawCircle(x, y, categoryCircleRadius, categoryCirclePaint)
    }

    private fun drawCategoryText(canvas: Canvas,
                                 x: Float,
                                 xWithPadding: Float,
                                 y: Float,
                                 middleXOffset: Float,
                                 middleYOffset: Float,
                                 textPadding: Float,
                                 jointX: Float,
                                 jointY: Float,
                                 text: String) {

        val bound = canvas.clipBounds
        when {
            jointX == middleXOffset -> {
                val textX = x + (textPadding / 1.5).toInt()
                val availableWidth = width - textX
                val areaWidth = width - availableWidth
                val width = bound.width() - areaWidth
                val newText = TextUtils.ellipsize(text, leftTextPaint, width, TextUtils.TruncateAt.END)
                canvas.drawText(
                    newText,
                    0,
                    newText.length,
                    textX,
                    y,
                    leftTextPaint
                )
            }
            jointY == middleYOffset && jointX <= middleXOffset -> {
                val ellipsizeSpace = width - xWithPadding
                val width = bound.width() - ellipsizeSpace
                val newText = TextUtils.ellipsize(text, rightTextPaint, width, TextUtils.TruncateAt.END)
                canvas.drawText(
                    newText,
                    0,
                    newText.length,
                    xWithPadding,
                    y,
                    rightTextPaint
                )
            }
            jointY == middleYOffset && jointX >= middleXOffset -> {
                val availableSpace = width - xWithPadding
                val areaWidth = width - availableSpace
                val width = bound.width() - areaWidth
                val newText = TextUtils.ellipsize(text, leftTextPaint, width, TextUtils.TruncateAt.END)
                canvas.drawText(
                    newText,
                    0,
                    newText.length,
                    xWithPadding,
                    y,
                    leftTextPaint
                )
            }
            jointX > middleXOffset -> {
                val availableSpace = width - x
                val areaWidth = width - availableSpace
                val width = bound.width() - areaWidth
                val newText = TextUtils.ellipsize(text, leftTextPaint, width, TextUtils.TruncateAt.END)
                canvas.drawText(
                    newText,
                    0,
                    newText.length,
                    x,
                    y,
                    leftTextPaint
                )
            }
            else -> {
                val areaWidth = width - x
                val width = bound.width() - areaWidth
                val newText = TextUtils.ellipsize(text, rightTextPaint, width, TextUtils.TruncateAt.END)
                canvas.drawText(
                    newText,
                    0,
                    newText.length,
                    x,
                    y,
                    rightTextPaint
                )
            }
        }
    }

    private fun drawCategories(canvas: Canvas,
                               x: Float,
                               y: Float,
                               radius: Float,
                               angleOfJoint: Int,
                               categoryCirclePadding: Float,
                               textPadding: Float,
                               text: String,
                               startColor: Int,
                               endColor: Int,
                               categoryCircleRadius: Float) {
        val circleX: Float = x + (cos(Math.toRadians(angleOfJoint.toDouble())) * (radius + categoryCirclePadding)).toFloat()
        val circleY: Float = y + (sin(Math.toRadians(angleOfJoint.toDouble())) * (radius + categoryCirclePadding)).toFloat()

        drawCategoryCircle(
            canvas = canvas,
            x = circleX,
            y = circleY,
            categoryCircleRadius = categoryCircleRadius,
            startColor = startColor,
            endColor = endColor
        )

        if (isDrawText) {
            val categoryTextX = x + (cos(Math.toRadians(angleOfJoint.toDouble())) * (radius + categoryCirclePadding + textPadding)).toFloat()
            val categoryTextXWithPadding: Float = x + (cos(Math.toRadians(angleOfJoint.toDouble())) * (radius + categoryCirclePadding + (textPadding * 0.65))).toFloat()
            val categoryTextY = circleY + 6.toDP(context)
            val jointX: Float = x + (cos(Math.toRadians(angleOfJoint.toDouble())) * radius).toFloat()
            val jointY: Float = y + (sin(Math.toRadians(angleOfJoint.toDouble())) * radius).toFloat()

            drawCategoryText(
                canvas = canvas,
                x = categoryTextX,
                xWithPadding = categoryTextXWithPadding,
                y = categoryTextY,
                jointX = jointX,
                jointY = jointY,
                textPadding = textPadding,
                middleXOffset = x,
                middleYOffset = y,
                text = text
            )
        }
    }

    private fun drawRadar(canvas: Canvas, x: Float, y: Float, radius: Float) {
        if(courseGroupGrades.isNotEmpty() && courseGroupGrades.size == numberOfCategoryJoints) {
            radarPath.reset()

            var angle = 360 / numberOfCategoryJoints
            var gradeIndex = 0
            for (i in 0 until 360 step angle) {
                val dispatchAngle = i - 90
                if (gradeIndex < courseGroupGrades.size) {
                    val grade = courseGroupGrades[gradeIndex].grade
                    val gradePointX = (x + (cos(Math.toRadians(dispatchAngle.toDouble()))) * (radius / numberOfRing) * grade).toFloat()
                    val gradePointY = (y + (sin(Math.toRadians(dispatchAngle.toDouble()))) * (radius / numberOfRing) * grade).toFloat()
                    if(gradeIndex == 0) {
                        radarPath.moveTo(gradePointX, gradePointY)
                        radarPath.lineTo(gradePointX, gradePointY)
                    } else {
                        radarPath.lineTo(gradePointX, gradePointY)
                    }
                }
                gradeIndex++
            }
            radarPath.close()

            val lightPrimaryColor = ColorUtils.blendARGB(radarColor, Color.WHITE, 0.4f)
            radarPaint.shader = LinearGradient(0f, 0f, width.toFloat(), 0f, lightPrimaryColor, radarColor, Shader.TileMode.CLAMP)
            canvas.drawPath(radarPath, radarPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = actualWidth.toInt()
        val desiredHeight = desiredViewHeight.toInt()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> widthSize.coerceAtMost(desiredWidth)
            MeasureSpec.UNSPECIFIED -> desiredWidth
            else -> desiredWidth
        }
        categoryCirclePadding = width.getPercentage(5)
        textPadding = width.getPercentage(6)
        val defaultPadding = width.getPercentage(15)
        val backgroundRadius = width/2 - categoryCirclePadding - textPadding - defaultPadding
        val categoryCircleRadius = backgroundRadius / 12
        val categoryCircleDiameter = categoryCircleRadius * 2
        val exactHeightSize = ((backgroundRadius * 2) + (categoryCirclePadding * 2) + (categoryCircleDiameter * 2)).toInt()

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> exactHeightSize
            MeasureSpec.AT_MOST -> exactHeightSize.coerceAtMost(desiredHeight)
            MeasureSpec.UNSPECIFIED -> desiredHeight
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    private fun setupStyleable(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarChartView)
        this.radarBackgroundColor = typedArray.getColor(R.styleable.RadarChartView_radarBackgroundColor, DEFAULT_RADAR_BACKGROUND_COLOR)
        this.radarColor = typedArray.getColor(R.styleable.RadarChartView_radarColor, DEFAULT_RADAR_COLOR)
        this.numberOfCategoryJoints = typedArray.getInt(R.styleable.RadarChartView_categoryNumber, DEFAULT_CATEGORY_JOINTS_NUMBER)
        this.innerLineWidth = typedArray.getDimension(R.styleable.RadarChartView_innerLineWidth, DEFAULT_INNER_LINE_WIDTH)
        this.radarInnerLineColor = typedArray.getColor(R.styleable.RadarChartView_innerLineColor, DEFAULT_INNER_LINE_COLOR)
        this.textColor = typedArray.getColor(R.styleable.RadarChartView_textColor, textColor)

        for (index in 0 until typedArray.indexCount) {
            val attribute = typedArray.getIndex(index)
            when (attribute) {
                R.styleable.RadarChartView_ssparkFont -> {
                    val fontTypeValue = typedArray.getInt(index, 0)
                    val fontTypeface = when (fontTypeValue) {
                        0 -> SSparkLibrary.boldTypeface
                        1 -> SSparkLibrary.boldSerifTypeface
                        2 -> SSparkLibrary.regularTypeface
                        3 -> SSparkLibrary.regularSerifTypeface
                        else -> SSparkLibrary.regularTypeface
                    }

                    textTypeface = fontTypeface
                }
            }
        }
        typedArray.recycle()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState: Parcelable? = super.onSaveInstanceState()
        superState?.let {
            val state = SavedState(superState)
            state.courseGroupGrades = this.courseGroupGrades.toJson()
            state.numberOfCategoryJoints = this.numberOfCategoryJoints
            state.isDrawText = this.isDrawText

            return state
        } ?: run {
            return superState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        when(state) {
            is SavedState -> {
                super.onRestoreInstanceState(state.superState)
                this.courseGroupGrades = state.courseGroupGrades.toObjects(Array<CourseGroupGrade>::class.java)
                this.numberOfCategoryJoints = state.numberOfCategoryJoints
                this.isDrawText = state.isDrawText
            }
            else -> {
                super.onRestoreInstanceState(state)
            }
        }
    }

    internal class SavedState: AbsSavedState {
        var courseGroupGrades = ""
        var numberOfCategoryJoints = 8
        var isDrawText = false

        constructor(superState: Parcelable): super(superState)
        constructor(source: Parcel, loader: ClassLoader?): super(source, loader) {
            numberOfCategoryJoints = source.readInt()
            courseGroupGrades = source.readString() ?: ""
            isDrawText = when (source.readByte()) {
                0.toByte() -> false
                else -> true
            }
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out , flags)
            out.writeInt(numberOfCategoryJoints)
            out.writeString(courseGroupGrades)
            val isDrawTextState = (if (isDrawText) 1 else 0).toByte()
            out.writeByte(isDrawTextState)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object: Parcelable.ClassLoaderCreator<SavedState> {
                override fun createFromParcel(source: Parcel, loader: ClassLoader): SavedState {
                    return SavedState(source, loader)
                }

                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source, null)
                }

                override fun newArray(size: Int): Array<SavedState> {
                    return newArray(size)
                }
            }
        }
    }
}