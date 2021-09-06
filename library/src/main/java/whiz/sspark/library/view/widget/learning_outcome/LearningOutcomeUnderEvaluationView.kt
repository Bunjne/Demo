package whiz.sspark.library.view.widget.learning_outcome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.LearningOutcome
import whiz.sspark.library.databinding.ViewLearningOutcomeUnderEvaluationItemBinding
import whiz.sspark.library.extension.show

class LearningOutcomeUnderEvaluationView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLearningOutcomeUnderEvaluationItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(learningOutcome: LearningOutcome) {
        binding.ivArrow.show(R.drawable.ic_arrow_right)

        with(learningOutcome) {
            binding.tvCourseCode.text = courseCode
            binding.tvCourseName.text = courseName
            binding.tvCredit.text = resources.getString(R.string.general_credit, credit.toString())
        }
    }
}