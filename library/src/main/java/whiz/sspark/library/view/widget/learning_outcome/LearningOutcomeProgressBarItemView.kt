package whiz.sspark.library.view.widget.learning_outcome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.LearningOutcome
import whiz.sspark.library.databinding.ViewLearningOutcomeProgressBarItemBinding
import whiz.sspark.library.extension.show

class LearningOutcomeProgressBarItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLearningOutcomeProgressBarItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(learningOutcome: LearningOutcome) {
        binding.ivArrow.show(R.drawable.ic_arrow_right)

        with(learningOutcome) {
            binding.tvCourseName.text = courseCode
            binding.tvCourseName.text = courseName
            binding.tvCredit.text = resources.getString(R.string.general_credit, credit.toString())
            binding.vProgressBar.progressColors = intArrayOf(startColor, endColor ?: startColor)
            binding.vProgressBar.progress = percentPerformance?.toFloat() ?: 0f
        }
    }
}