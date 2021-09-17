package whiz.sspark.library.view.widget.learning_pathway.add_course

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Course
import whiz.sspark.library.databinding.ViewCourseBinding
import whiz.sspark.library.extension.show

class CourseItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewCourseBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(course: Course) {
        binding.ivArrow.show(R.drawable.ic_arrow_right)

        with(course) {
            binding.tvCourseCode.text = code
            binding.tvCourseName.text = name
            binding.tvCredit.text = resources.getString(R.string.general_credit, credit.toString())
        }
    }
}