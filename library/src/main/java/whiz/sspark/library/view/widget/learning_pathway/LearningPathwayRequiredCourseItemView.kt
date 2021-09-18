package whiz.sspark.library.view.widget.learning_pathway

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Course
import whiz.sspark.library.data.entity.LearningPathwayRequiredCourseItem
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.databinding.ViewLearningPathwayRequiredCourseItemBinding
import whiz.sspark.library.extension.show

class LearningPathwayRequiredCourseItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLearningPathwayRequiredCourseItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(requiredCoursesItem: LearningPathwayRequiredCourseItem,
             onClicked: (Term, List<Course>) -> Unit) {
        setOnClickListener {
            onClicked(requiredCoursesItem.term, requiredCoursesItem.courses)
        }

        binding.ivLock.show(R.drawable.ic_lock)
        binding.ivArrow.show(R.drawable.ic_arrow_right)

        binding.tvCourseCount.text = resources.getString(R.string.learning_pathway_required_course_count, requiredCoursesItem.courses.size.toString())
    }
}