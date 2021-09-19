package whiz.sspark.library.view.widget.course_syllabus

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.databinding.ViewCourseSyllabusHeaderItemBinding

class CourseSyllabusTitleItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewCourseSyllabusHeaderItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String) {
        binding.tvTitle.text = title
    }
}