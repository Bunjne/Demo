package whiz.sspark.library.view.widget.exam_schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ExamScheduleCourse
import whiz.sspark.library.databinding.ViewExamScheduleCourseItemBinding
import whiz.sspark.library.extension.convertToTime

class ExamScheduleCourseItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewExamScheduleCourseItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(examScheduleCourse: ExamScheduleCourse) {
        with(examScheduleCourse) {
            binding.tvTimeRange.text = resources.getString(R.string.class_schedule_range, startTime.convertToTime(), endTime.convertToTime())
            binding.tvCourse.text = resources.getString(R.string.class_schedule_course_code_and_name, code, name)
            binding.tvRoom.text = resources.getString(R.string.exam_schedule_room, room)
        }
    }
}