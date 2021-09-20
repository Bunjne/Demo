package whiz.sspark.library.view.widget.collaboration.course_syllabus.detail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.databinding.ViewCourseSyllabusDetailTopBinding

class CourseSyllabusDetailTopItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewCourseSyllabusDetailTopBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(position: String?,
             detail: String) {
        if (position != null) {
            binding.tvPosition.text = position.toString()
            binding.tvPosition.visibility = View.VISIBLE
        } else {
            binding.tvPosition.visibility = View.GONE
        }

        binding.tvCourseDetail.text = detail
    }
}