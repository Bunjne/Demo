package whiz.sspark.library.view.widget.collaboration.class_group

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassGroupCourse
import whiz.sspark.library.data.enum.Gender
import whiz.sspark.library.databinding.ViewClassGroupItemBinding
import whiz.sspark.library.extension.*

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
                binding.tvNotificationCount.text = notificationCount.toString()
                binding.cvNotificationCount.visibility = View.VISIBLE
            }

            if (instructors.isNotEmpty()) {
                val mainInstructor = instructors.first()
                binding.civInstructorImage.showClassMemberProfileCircle(mainInstructor.profileImageUrl, mainInstructor.abbreviatedName, Color.WHITE, mainInstructor.colorCode?.toColor() ?: Color.BLACK)

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
            binding.ivMember.show(R.drawable.ic_user)
            binding.tvMemberCount.text = studentCount.toString()
        }

        setOnClickListener {
            onClassGroupItemClicked(classGroupCourse)
        }
    }
}