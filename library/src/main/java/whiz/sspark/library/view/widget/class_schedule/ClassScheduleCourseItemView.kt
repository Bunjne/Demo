package whiz.sspark.library.view.widget.class_schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassScheduleCourse
import whiz.sspark.library.databinding.ViewClassScheduleCourseItemBinding
import whiz.sspark.library.extension.convertToTime
import whiz.sspark.library.extension.toColor

class ClassScheduleCourseItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassScheduleCourseItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(classScheduleCourse: ClassScheduleCourse) {
        with(classScheduleCourse) {
            val convertedInstructorName = instructorNames.joinToString(",") { it }

            binding.cvVerticalBar.setCardBackgroundColor(color.toColor())
            binding.tvTimeRange.text = resources.getString(R.string.class_schedule_range, startTime.convertToTime(), endTime.convertToTime())
            binding.tvCourse.text = title
            binding.tvInstructorAndRoom.text = resources.getString(R.string.class_schedule_instructor_and_room, convertedInstructorName, room)
        }
    }
}