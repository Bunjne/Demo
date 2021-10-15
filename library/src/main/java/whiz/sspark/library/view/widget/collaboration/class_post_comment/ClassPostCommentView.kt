package whiz.sspark.library.view.widget.collaboration.class_post_comment

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Comment
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewClassPostCommentBinding
import whiz.sspark.library.extension.showProfile
import whiz.sspark.library.extension.toPostTime
import whiz.sspark.library.utility.convertToFullName

class ClassPostCommentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassPostCommentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(comment: Comment,
             color: Int,
             onItemClicked: (Comment) -> Unit) {
        with(comment) {
            binding.cvProfile.showProfile(author.imageUrl, getGender(author.gender).type)

            val commentAuthorName = if (author.position.isNotBlank()) {
                convertToFullName(
                    position = author.position,
                    firstName = author.firstName,
                    middleName = author.middleName,
                    lastName = author.lastName
                )
            } else {
                resources.getString(R.string.class_post_comment_author_name_place_holder, author.number, author.nickname)
            }

            binding.tvName.text = commentAuthorName
            binding.tvDate.text = resources.getString(R.string.date_time_place_holder, datetime.toPostTime(context))
            with(binding.tvMessage) {
                text = message
                setLinkTextColor(color)
            }
        }

        binding.clCommentContainer.setOnClickListener {
            onItemClicked(comment)
        }
    }
}