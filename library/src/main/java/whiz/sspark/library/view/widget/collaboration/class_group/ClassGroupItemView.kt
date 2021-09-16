package whiz.sspark.library.view.widget.collaboration.class_group

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassGroupCourse
import whiz.sspark.library.data.enum.Gender
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewClassGroupItemBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle
import whiz.sspark.library.extension.withAlpha

class ClassGroupItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassGroupItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(classGroupCourse: ClassGroupCourse,
             onClassGroupItemClicked: (ClassGroupCourse) -> Unit) {
        with (classGroupCourse) {
            if (notificationCount == 0) {
                binding.cvNotificationCount.visibility = View.INVISIBLE
            } else {
                binding.tvNotificationCount.text = classGroupCourse.notificationCount.toString()
                binding.cvNotificationCount.visibility = View.VISIBLE
                binding.cvNotificationCount.post {
                    binding.cvNotificationCount.cornerRadius_ = (binding.cvNotificationCount.height / 2).toFloat()
                    binding.cvNotificationCount.invalidate()
                }
            }


            if (instructors.isNotEmpty()) {
                val mainInstructor = instructors.first()
                binding.civInstructorImage.showUserProfileCircle(mainInstructor.profileImageUrl, getGender(mainInstructor.gender).type)

                val otherInstructorCount = instructors.size - 1
                if (otherInstructorCount == 0) {
                    binding.cvOtherInstructorCount.visibility = View.INVISIBLE
                } else {
                    binding.cvOtherInstructorCount.visibility = View.VISIBLE
                    binding.tvOtherInstructorCount.text = resources.getString(R.string.class_group_other_instructor_count_place_holder, otherInstructorCount)
                }
            } else {
                binding.cvOtherInstructorCount.visibility = View.INVISIBLE
                binding.civInstructorImage.showUserProfileCircle("", Gender.NOTSPECIFY.type)
            }

            binding.tvCourseCode.text = courseCode
            binding.tvCourseName.text = courseName

            binding.cvMemberCount.setCardBackgroundColor(ContextCompat.getColor(context, R.color.textBaseThirdColor).withAlpha(34))
            binding.tvMemberCount.text = studentCount.toString()
        }
    }
}