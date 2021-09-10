package whiz.sspark.library.view.widget.collaboration.class_post_comment

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.databinding.ViewClassPostCommentBinding
import whiz.sspark.library.extension.showClassMemberProfileCircle
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.extension.toInstructorFullName
import whiz.sspark.library.extension.toPostTime

class ClassPostCommentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassPostCommentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(post: Post,
             color: Int,
             onItemClicked: (Post) -> Unit) {
        with(post) {

            val authorColor = if (author.colorCode.isNullOrBlank()) {
                Color.BLACK
            } else {
                author.colorCode.toColor()
            }

            binding.cvProfile.showClassMemberProfileCircle(author.profileImageUrl, author, Color.WHITE, authorColor)

            val fullName = author.toInstructorFullName()

            binding.tvName.text = fullName
            binding.tvDate.text = createdAt.toPostTime(context)
            with(binding.tvMessage) {
                text = message
                setLinkTextColor(color)
            }
        }

        binding.clCommentContainer.setOnClickListener {
            onItemClicked(post)
        }
    }
}