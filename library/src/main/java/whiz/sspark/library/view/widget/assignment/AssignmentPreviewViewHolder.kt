package whiz.sspark.library.view.widget.assignment

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.data.enum.AttachmentType
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewAssignmentPreviewItemBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.extension.toPostTime

class AssignmentPreviewViewHolder(
    private val binding: ViewAssignmentPreviewItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun init(context:Context, assignment: Assignment) {
        binding.ivArrow.show(R.drawable.ic_arrow_right)

        with(assignment) {
            binding.cvVerticalBar.setCardBackgroundColor(color.toColor(ContextCompat.getColor(context, R.color.viewBaseFourthColor)))
            binding.tvCourseName.text = courseName
            binding.tvDate.text = createdAt.toPostTime(context)
            binding.tvTitle.text = title
            binding.tvDescription.text = description
            binding.tvDeadline.text = context.resources.getString(R.string.assignment_deadline, deadlineAt.toPostTime(context))
            binding.ivInstructorImage.showUserProfileCircle(imageUrl, getGender(gender).type)
            binding.tvInstructorName.text = instructorName
        }
    }
}