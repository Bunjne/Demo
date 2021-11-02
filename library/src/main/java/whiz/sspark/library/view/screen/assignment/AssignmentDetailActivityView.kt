package whiz.sspark.library.view.screen.assignment

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.data.enum.AttachmentType
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewAssignmentDetailActivityBinding
import whiz.sspark.library.extension.showUserProfileCircle
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.extension.toLocalDate
import whiz.sspark.library.extension.toPostTime
import whiz.sspark.library.view.widget.collaboration.class_activity.post.ClassPostFileView
import whiz.sspark.library.view.widget.collaboration.class_activity.post.ClassPostImageView

class AssignmentDetailActivityView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAssignmentDetailActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(assignment: Assignment,
             onFileClicked: (Attachment) -> Unit,
             navigateToImage: (ImageView, Attachment) -> Unit) {
        with(assignment) {
            binding.cvVerticalBar.setCardBackgroundColor(startColor.toColor(ContextCompat.getColor(context, R.color.viewBaseFourthColor)))
            binding.tvCourseName.text = courseTitle
            binding.tvDate.text = createdAt.toLocalDate()?.toPostTime(context)
            binding.tvTitle.text = title
            binding.tvDescription.text = description
            binding.tvDeadline.text = context.resources.getString(R.string.assignment_deadline, deadlineAt.toLocalDate()?.toPostTime(context))
            binding.ivInstructorImage.showUserProfileCircle(imageUrl, getGender(gender).type)
            binding.tvInstructorName.text = instructorName

            if (attachments.isNotEmpty()) {
                binding.llAttachment.visibility = View.VISIBLE
                binding.llAttachment.removeAllViews()

                attachments.forEach {
                    if (it.type == AttachmentType.IMAGE.type) {
                        binding.llAttachment.addView(ClassPostFileView(context).apply {
                            init(it, onFileClicked)
                        })
                    } else {
                        binding.llAttachment.addView(ClassPostImageView(context).apply {
                            init(it, navigateToImage)
                        })
                    }
                }
            }
        }
    }
}