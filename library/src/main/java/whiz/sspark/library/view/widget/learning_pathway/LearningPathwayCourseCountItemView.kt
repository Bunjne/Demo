package whiz.sspark.library.view.widget.learning_pathway

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewLearningPathwayCourseCountItemBinding

class LearningPathwayCourseCountItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLearningPathwayCourseCountItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(count: Int) {
        binding.tvCourseCount.text = resources.getString(R.string.learning_pathway_concentrate_course_count, count.toString())
    }
}