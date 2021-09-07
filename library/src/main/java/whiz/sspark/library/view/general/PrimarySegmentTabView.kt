package whiz.sspark.library.view.general

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
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
             onTabClicked: (Int) -> Unit) {
        this.onTabClicked = onTabClicked

        binding.rgContainer.removeAllViews()

        if (titles.size == 1) {
            val title = titles.getOrElse(0) { "" }

            val singleRadioButton = getSegmentRadioButton(context, 0, title).apply {
                setTextColor(ContextCompat.getColor(context, R.color.textBasePrimaryColor))
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
            }

            setSegmentListener(onTabClicked)
            binding.rgContainer.addView(singleRadioButton)
        } else {
            titles.forEachIndexed { index, title ->
                val radioButton = getSegmentRadioButton(context, index, title).apply {
                    background = ContextCompat.getDrawable(context, R.drawable.selector_primary_segment)
                }

                binding.rgContainer.addView(radioButton)
            }

            setSegmentListener(onTabClicked)
        }

        onTabClicked(0)
    }

    private fun setSegmentListener(onTabClicked: (Int) -> Unit) {
        binding.rgContainer.setOnCheckedChangeListener(null)
        binding.rgContainer.setOnCheckedChangeListener { _, id ->
            onTabClicked(id)
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
        setTextColor(ContextCompat.getColorStateList(context, R.color.selector_text_primary_segment))

        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(this, 10, 14, 1, TypedValue.COMPLEX_UNIT_DIP)
        maxLines = 1
        text = title
    }

    fun selectTab(index: Int) {
        binding.rgContainer.check(index)
        onTabClicked(index)
    }
}