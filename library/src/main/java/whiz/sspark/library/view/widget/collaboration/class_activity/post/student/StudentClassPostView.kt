package whiz.sspark.library.view.widget.collaboration.class_activity.post.student

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.databinding.ViewStudentClassPostBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.toAttachmentFiles
import whiz.sspark.library.extension.toAttachmentImages
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.view.widget.collaboration.class_activity.post.ClassPostFileView
import whiz.sspark.library.view.widget.collaboration.class_activity.post.ClassPostImageView

class StudentClassPostView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewStudentClassPostBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val defaultColor by lazy {
        ContextCompat.getColor(context, R.color.textBasePrimaryColor)
    }

    fun init(post: Post,
             allMemberCount: Int,
             color: Int,
             onPostClicked: (Post) -> Unit = {},
             onImageClicked: (ImageView, Attachment) -> Unit,
             onLikeClicked: (Post) -> Unit,
             onCommentClicked: (Post) -> Unit = {},
             onFileClicked: (Attachment) -> Unit,
             onDisplayLikedUsersClicked: (String) -> Unit,
             onDisplaySeenUsersClicked: (String) -> Unit) {
        binding.vAuthor.init(post.instructor, post.createdAt, post.updatedAt, post.isRead, color)
        with(binding.tvPostText) {
            text = post.message
            setLinkTextColor(color)
        }

        val images = post.attachments.toAttachmentImages()

        binding.llPostImage.removeAllViews()
        if (images.isEmpty()) {
            binding.llPostImage.visibility = View.GONE
        } else {
            binding.llPostImage.visibility = View.VISIBLE
            images.forEach { image ->
                binding.llPostImage.addView(
                    ClassPostImageView(context).apply {
                        init(image, onImageClicked)
                    }
                )
            }
        }

        binding.llPostFile.removeAllViews()
        val files = post.attachments.toAttachmentFiles()

        if (files.isEmpty()) {
            binding.llPostFile.visibility = View.GONE
        } else {
            binding.llPostFile.visibility = View.VISIBLE
            files.forEach { file ->
                binding.llPostFile.addView(
                    ClassPostFileView(context).apply {
                        init(file, onFileClicked)
                    }
                )
            }
        }

        binding.ivLike.show(R.drawable.ic_like)
        binding.ivComment.show(R.drawable.ic_comment)

        if (post.isLike) {
            binding.ivLike.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            binding.tvLike.setTextColor(color)
        } else {
            binding.ivLike.setColorFilter(defaultColor, PorterDuff.Mode.SRC_ATOP)
            binding.tvLike.setTextColor(defaultColor)
        }

        if (post.likeCount > 0 && post.commentCount > 0) {
            binding.tvLikeCount.visibility = View.VISIBLE
            binding.tvInterpunct.visibility = View.VISIBLE
            binding.tvCommentCount.visibility = View.VISIBLE

            binding.tvLikeCount.text = resources.getQuantityString(R.plurals.class_post_count_like, post.likeCount, post.likeCount)
            binding.tvCommentCount.text = resources.getQuantityString(R.plurals.class_post_count_comment, post.commentCount, post.commentCount)
        } else if (post.likeCount <= 0 && post.commentCount > 0) {
            binding.tvLikeCount.visibility = View.GONE
            binding.tvInterpunct.visibility = View.GONE
            binding.tvCommentCount.visibility = View.VISIBLE

            binding.tvCommentCount.text = resources.getQuantityString(R.plurals.class_post_count_comment, post.commentCount, post.commentCount)
        } else if (post.likeCount > 0 && post.commentCount <= 0) {
            binding.tvLikeCount.visibility = View.VISIBLE
            binding.tvInterpunct.visibility = View.GONE
            binding.tvCommentCount.visibility = View.GONE

            binding.tvLikeCount.text = resources.getQuantityString(R.plurals.class_post_count_like, post.likeCount, post.likeCount)
        } else {
            binding.tvLikeCount.visibility = View.GONE
            binding.tvInterpunct.visibility = View.GONE
            binding.tvCommentCount.visibility = View.GONE
        }

        val readCount = post.readCount
        with(binding.tvReadCount) {
            text = if (readCount == allMemberCount) {
                resources.getString(R.string.class_post_count_read_everyone)
            } else {
                resources.getString(R.string.class_post_count_read, readCount)
            }
            visibility = if (readCount <= 0) View.GONE else View.VISIBLE
        }

        setOnClickListener {
            onPostClicked(post)
        }
        binding.clComment.setOnClickListener {
            onCommentClicked(post)
        }
        binding.clLike.setOnClickListener {
            onLikeClicked(post)
        }

        listOf(binding.tvLikeCount, binding.tvCommentCount, binding.tvInterpunct).forEach {
            it.setOnClickListener {
                if (post.likeCount > 0) {
                    onDisplayLikedUsersClicked(post.id)
                }
            }
        }

        binding.tvReadCount.setOnClickListener {
            onDisplaySeenUsersClicked(post.id)
        }
    }
}