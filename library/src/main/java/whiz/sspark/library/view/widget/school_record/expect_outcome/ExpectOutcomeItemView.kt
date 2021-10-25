package whiz.sspark.library.view.widget.school_record.expect_outcome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.ExpectOutcomeCourse
import whiz.sspark.library.databinding.ViewExpectOutcomeCourseItemBinding

class ExpectOutcomeItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewExpectOutcomeCourseItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(expectOutcomeCourse: ExpectOutcomeCourse) {
        with(expectOutcomeCourse) {
            binding.tvTitle.text = title
            binding.tvDescription.text = description
            binding.vProgressBar.initGradientColor(startColor, endColor)
            binding.vProgressBar.init(value, indicators)
        }
    }
}