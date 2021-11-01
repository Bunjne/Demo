package whiz.sspark.library.view.widget.assignment

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewAssignmentPreviewItemBinding
import whiz.sspark.library.extension.*
import whiz.sspark.library.extension.toPostTime

class AssignmentPreviewViewHolder(
    private val binding: ViewAssignmentPreviewItemBinding
): RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context

    fun init(assignment: Assignment) {
        binding.ivArrow.show(R.drawable.ic_arrow_right)

        with(assignment) {
            binding.cvVerticalBar.setCardBackgroundColor(startColor.toColor(ContextCompat.getColor(context, R.color.viewBaseFourthColor)))
            binding.tvCourseName.text = courseTitle
            binding.tvDate.text = createdAt.toLocalDate()?.toPostTime(context)
            binding.tvTitle.text = title
            binding.tvDescription.text = description
            binding.tvDeadline.text = context.resources.getString(R.string.assignment_deadline, deadlineAt.toLocalDate()?.toPostFullDateTime(context))
            binding.ivInstructorImage.showUserProfileCircle(imageUrl, getGender(gender).type)
            binding.tvInstructorName.text = instructorName
        }
    }
}