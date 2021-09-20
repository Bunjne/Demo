package whiz.sspark.library.view.widget.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.InstructorComment
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewBaseInstructorCommentBinding
import whiz.sspark.library.extension.showUserProfileCircle
import whiz.sspark.library.extension.toPostTime

class InstructorCommentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewBaseInstructorCommentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(instructorComment: InstructorComment) {
        with(instructorComment) {
            binding.ivProfile.showUserProfileCircle(imageUrl, getGender(gender).type)
            binding.tvName.text = name
            binding.tvComment.text = comment
            binding.tvCreatedAt.text = createdAt.toPostTime(context)
        }
    }
}