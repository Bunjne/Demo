package whiz.sspark.library.view.general.segment

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
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
import whiz.sspark.library.databinding.ViewSegmentTabBinding
import whiz.sspark.library.extension.toDP

class SegmentTabView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewSegmentTabBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var onTabClicked: (Int) -> Unit = { }
    private var segmentTextColorStateList: ColorStateList? = null
    private var segmentBackgroundDrawable: Drawable? = null

    fun init(titles: List<String>,
             onTabClicked: (Int) -> Unit,
             initialTab: Int = 0,
             textColorStateList: ColorStateList? = null,
             backgroundDrawable: Drawable? = null) {
        this.onTabClicked = onTabClicked

        segmentBackgroundDrawable = backgroundDrawable
        segmentTextColorStateList = textColorStateList

        segmentBackgroundDrawable?.let {
            binding.rgContainer.background = segmentBackgroundDrawable
        }

        binding.rgContainer.removeAllViews()

        titles.forEachIndexed { index, title ->
            val radioButton = getSegmentRadioButton(context, index, initialTab, title).apply {
                if (segmentTextColorStateList != null) {
                    setTextColor(segmentTextColorStateList)
                } else {
                    setTextColor(ContextCompat.getColor(context, R.color.textBasePrimaryColor))
                }
            }
            binding.rgContainer.addView(radioButton)
        }

        setSegmentListener(onTabClicked)
        onTabClicked(initialTab)
    }

    fun updateSegmentTitle(titles: List<String>, currentSegment: Int) {
        binding.rgContainer.removeAllViews()
        titles.forEachIndexed { index, title ->
            val radioButton = getSegmentRadioButton(context, index, currentSegment, title).apply {
                if (segmentTextColorStateList != null) {
                    setTextColor(segmentTextColorStateList)
                } else {
                    setTextColor(ContextCompat.getColor(context, R.color.textBasePrimaryColor))
                }
            }
            binding.rgContainer.addView(radioButton)
        }
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

    private fun getSegmentRadioButton(context: Context,
                                      resourceId: Int,
                                      selectedSegment: Int,
                                      title: String) = RadioButton(context).apply {
        if (resourceId == selectedSegment) {
            isChecked = true
        }
        id = resourceId
        layoutParams = RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
            topMargin = 2.toDP(context)
            bottomMargin = 2.toDP(context)
            rightMargin = 2.toDP(context)
            leftMargin = 2.toDP(context)
        }
        setPadding(6.toDP(context), 4.toDP(context), 6.toDP(context), 4.toDP(context))
        gravity = Gravity.CENTER
        buttonDrawable = null
        typeface = SSparkLibrary.boldTypeface
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(this, 10, 15, 1, TypedValue.COMPLEX_UNIT_DIP)
        maxLines = 1
        text = title
        background = ContextCompat.getDrawable(context, R.drawable.selector_base_segment)
    }

    fun selectTab(index: Int) {
        binding.rgContainer.clearCheck()
        binding.rgContainer.check(index)
    }
}