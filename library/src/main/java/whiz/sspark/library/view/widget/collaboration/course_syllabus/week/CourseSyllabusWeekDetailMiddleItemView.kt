package whiz.sspark.library.view.widget.collaboration.course_syllabus.week

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.databinding.ViewCourseSyllabusWeekDetailMiddleBinding

class CourseSyllabusWeekDetailMiddleItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewCourseSyllabusWeekDetailMiddleBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(detail: String) {
        binding.tvCourseDetail.text = detail
    }
}