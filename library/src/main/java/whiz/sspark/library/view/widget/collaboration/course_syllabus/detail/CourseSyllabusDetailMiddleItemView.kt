package whiz.sspark.library.view.widget.collaboration.course_syllabus.detail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.databinding.ViewCourseSyllabusDetailMiddleBinding

class CourseSyllabusDetailMiddleItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewCourseSyllabusDetailMiddleBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(position: Int?,
             detail: String) {
        if (position != null) {
            binding.tvPosition.visibility = View.VISIBLE
            binding.tvPosition.text = position.toString()
        } else {
            binding.tvPosition.visibility = View.GONE
        }

        binding.tvCourseDetail.text = detail
    }
}