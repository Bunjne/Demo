package whiz.sspark.library.view.general

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import whiz.sspark.library.R
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.databinding.ViewPrimarySegmentTabBinding
import whiz.sspark.library.extension.toDP

class PrimarySegmentTabView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewPrimarySegmentTabBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var onTabClicked: (Int) -> Unit = { }

    fun init(titles: Array<String>,
             onTabClicked: (Int) -> Unit,
             gradientColors: List<Int>) {
        this.onTabClicked = onTabClicked

        binding.rgContainer.removeAllViews()

        initSegmentContainer()

        if (titles.size == 1) {
            val title = titles.getOrElse(0) { "" }

            val singleRadioButton = getSegmentRadioButton(context, 0, title).apply {
                id = 0
                setTextColor(ContextCompat.getColor(context, R.color.textBasePrimaryColor))
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
                background = getCorneredCenterTabBackground(gradientColors)
            }

            setSegmentListener(onTabClicked)
            binding.rgContainer.addView(singleRadioButton)
        } else {
            titles.forEachIndexed { index, title ->
                val radioButton = getSegmentRadioButton(context, index, title).apply {
                    id = index
                }
                when (index) {
                    0 -> binding.rgContainer.addView(radioButton.apply {
                        background = getLeftStripSelector(gradientColors)
                    })
                    titles.lastIndex -> binding.rgContainer.addView(radioButton.apply {
                        background = getRightStripSelector(gradientColors)
                    })
                    else -> binding.rgContainer.addView(radioButton.apply {
                        background = getCenterStripSelector(gradientColors)
                    })
                }
            }

            setSegmentListener(onTabClicked)
        }

        onTabClicked(0)
    }

    fun hideSegmentTab(idTab: Int) {
        binding.rgContainer.getChildAt(idTab).visibility = View.GONE
    }

    fun getCurrentSelectedId(): Int = binding.rgContainer.checkedRadioButtonId

    private fun setSegmentListener(onTabClicked: (Int) -> Unit) {
        binding.rgContainer.setOnCheckedChangeListener(null)
        binding.rgContainer.setOnCheckedChangeListener { _, id ->
            onTabClicked(id)
        }
    }

    private fun initSegmentContainer() {
        with(binding.rgContainer) {
            dividerDrawable = getSegmentDivider()
            background = getSegmentBackground()
        }
    }

    private fun getSegmentRadioButton(context: Context,
                                      resourceId: Int,
                                      title: String) = RadioButton(context).apply {
        if (resourceId == 0) {
            isChecked = true
        }
        id = resourceId
        layoutParams = RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
            topMargin = 2.toDP(context)
            bottomMargin = 2.toDP(context)
            rightMargin = 2.toDP(context)
            leftMargin = 2.toDP(context)
        }
        setPadding(8.toDP(context), 6.toDP(context), 8.toDP(context), 6.toDP(context))
        gravity = Gravity.CENTER
        buttonDrawable = null
        typeface = SSparkLibrary.boldTypeface
        setTextColor(getStatedTextColor())

        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(this, 10, 14, 1, TypedValue.COMPLEX_UNIT_SP)
        maxLines = 1
        text = title
    }

    private fun getStatedTextColor() = ColorStateList(
            arrayOf(
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(Color.WHITE, ContextCompat.getColor(context, R.color.textBasePrimaryColor))
    )

    private fun getSegmentDivider() = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setSize(1, 0)
        setColor(ContextCompat.getColor(context, R.color.viewBaseThirdColor))
    }

    private fun getSegmentBackground() = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setSize(1f.toDP(context).toInt(), 0)
        setColor(ContextCompat.getColor(context, R.color.viewBaseSecondaryColor))
        cornerRadius = 16f.toDP(context)
    }

    private fun getCorneredCenterTabBackground(gradientColors: List<Int>): Drawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 16f.toDP(context)
            colors = gradientColors.toIntArray()
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
        }
    }

    private fun getCenterStripSelector(gradientColors: List<Int>) = StateListDrawable().apply {
        val cornerRadius = 16f.toDP(context)

        val filledLeftStripDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius)
            colors = gradientColors.toIntArray()
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
        }

        val leftStripDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
        }

        addState(intArrayOf(android.R.attr.state_checked), filledLeftStripDrawable)
        addState(intArrayOf(-android.R.attr.state_checked), leftStripDrawable)
    }

    private fun getLeftStripSelector(gradientColors: List<Int>) = StateListDrawable().apply {

        val cornerRadius = 16f.toDP(context)

        val filledLeftStripDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius)
            colors = gradientColors.toIntArray()
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
        }

        val leftStripDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius)
        }

        addState(intArrayOf(android.R.attr.state_checked), filledLeftStripDrawable)
        addState(intArrayOf(-android.R.attr.state_checked), leftStripDrawable)
    }

    private fun getRightStripSelector(gradientColors: List<Int>) = StateListDrawable().apply {

        val cornerRadius = 16f.toDP(context)

        val filledRightStripDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius)
            colors = gradientColors.toIntArray()
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
        }

        val rightStripDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius)
        }

        addState(intArrayOf(android.R.attr.state_checked), filledRightStripDrawable)
        addState(intArrayOf(-android.R.attr.state_checked), rightStripDrawable)
    }

    fun selectTab(index: Int) {
        binding.rgContainer.check(index)
        onTabClicked(index)
    }
}