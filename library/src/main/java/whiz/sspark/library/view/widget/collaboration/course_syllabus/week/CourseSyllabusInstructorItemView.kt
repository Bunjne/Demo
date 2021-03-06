package whiz.sspark.library.view.widget.collaboration.course_syllabus.week

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewCourseSyllabusInstructorItemBinding
import whiz.sspark.library.extension.show

class CourseSyllabusInstructorItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewCourseSyllabusInstructorItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(instructors: List<String>) {
        val convertedInstructor = instructors.joinToString(", ")
        binding.tvInstructor.text = convertedInstructor
        binding.ivInstructor.show(R.drawable.ic_human)
    }
}