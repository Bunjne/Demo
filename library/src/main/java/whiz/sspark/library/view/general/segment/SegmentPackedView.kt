package whiz.sspark.library.view.general.segment

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.databinding.ViewSegmentPackedBinding

class SegmentPackedView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewSegmentPackedBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(selectTab: Int = 0,
             titles: List<String>,
             onTabClicked: (Int) -> Unit) {
        binding.rgContainer.removeAllViews()
        binding.rgContainer.setOnCheckedChangeListener { _, id ->
            onTabClicked(id)
        }

        titles.forEachIndexed { index, title ->
            val radioButton = getSegmentRadioButton(context, selectTab, index, title)
            binding.rgContainer.addView(radioButton)
        }

        onTabClicked(selectTab)
    }

    fun setSelectedTab(index: Int) {
        binding.rgContainer.clearCheck()
        binding.rgContainer.check(index)
    }

    private fun getSegmentRadioButton(context: Context,
                                      selectTab: Int,
                                      resourceId: Int,
                                      title: String) = RadioButton(context).apply {
        if (resourceId == selectTab) {
            isChecked = true
        }
        id = resourceId
        background = ContextCompat.getDrawable(context, R.drawable.selector_menu_student_segment)
        layoutParams = RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)
        buttonDrawable = null
        typeface = SSparkLibrary.regularTypeface
        setTextColor(
            ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(ContextCompat.getColor(context, R.color.textBasePrimaryColor), ContextCompat.getColor(context, R.color.textBaseThirdColor))
        ))
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
        maxLines = 1
        text = title
    }
}