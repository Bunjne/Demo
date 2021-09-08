package whiz.sspark.library.view.widget.learning_outcome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.CourseGroupGrade
import whiz.sspark.library.databinding.ViewLearningOutcomeGradeSummaryItemBinding

class LearningOutcomeGradeSummaryItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLearningOutcomeGradeSummaryItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(gradeSummary: List<CourseGroupGrade>) {
        binding.vGradeSummary.init(
            courseGroupGrades = gradeSummary,
            isDrawText = true,
            numberOfCategory = gradeSummary.size
        )
    }
}