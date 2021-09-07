package whiz.sspark.library.view.widget.collaboration.class_activity.post

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.databinding.ViewClassPostBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.toObjects

class ClassPostView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassPostBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val defaultColor by lazy {
        ContextCompat.getColor(context, R.color.textBaseThirdColor)
    }

    fun init(post: Post,
             allMemberCount: Int,
             color: Int,
             onPostRead: (Any) -> Unit = {},
             onPostClicked: (Post) -> Unit = {},
             onDeletePostClicked: (Post) -> Unit,
             onEditPostClicked: (Post) -> Unit,
             onImageClicked: (ImageView, Attachment) -> Unit,
             onLikeClicked: (Post) -> Unit,
             onCommentClicked: (Post) -> Unit = {},
             onFileClicked: (Attachment) -> Unit,
             onDisplayLikedUsersClicked: (Any) -> Unit,
             onDisplaySeenUsersClicked: (Any) -> Unit) {
        binding.ivMore.show(R.drawable.ic_post_option)
        if (!post.isRead) {
            onPostRead(post.id)
        }
        binding.vAuthor.init(post.author, post.createdAt, post.updatedAt, post.isRead, color)
        with(binding.tvPostText) {
            text = post.message
            setLinkTextColor(color)
        }

        with(binding.ivMore) {
            setOnClickListener {
                PopupMenu(context, binding.ivMore).run {
                    setOnMenuItemClickListener {
                        if (it.itemId == R.id.menuDelete) {
                            onDeletePostClicked(post)
                        } else {
                            onEditPostClicked(post)
                        }
                        true
                    }
                    inflate(R.menu.menu_class_post)
                    show()
                }
            }

            visibility = if (USparkLibrary.isStudent) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        if (post.attachments.isNotBlank()) {
            val attachments = post.attachments.toObjects(Array<Attachment>::class.java)
            val images = attachments.toAttachmentImages()

            binding.llPostImage.removeAllViews()
            if (images.isEmpty()) {
                binding.llPostImage.visibility = View.GONE
            } else {
                binding.llPostImage.visibility = View.VISIBLE
                images.forEach { image ->
                    binding.llPostImage.addView(
                        ClassPostImageView(
                            context
                        ).apply {
                        init(image, onImageClicked)
                    })
                }
            }

            binding.llPostFile.removeAllViews()
            val files = attachments.toAttachmentFiles()

            if (files.isEmpty()) {
                binding.llPostFile.visibility = View.GONE
            } else {
                binding.llPostFile.visibility = View.VISIBLE
                files.forEach { file ->
                    binding.llPostFile.addView(
                        ClassPostFileView(
                            context
                        ).apply {
                        init(file, onFileClicked)
                    })
                }
            }
        } else {
            binding.llPostImage.visibility = View.GONE
            binding.llPostFile.visibility = View.GONE
        }

        if (post.isLike) {
            binding.ivLike.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            binding.tvLike.setTextColor(color)
        } else {
            binding.ivLike.setColorFilter(defaultColor, PorterDuff.Mode.SRC_ATOP)
            binding.tvLike.setTextColor(defaultColor)
        }

        with(binding.tvLikeCommentCount) {
            val likeString = resources.getQuantityString(R.plurals.class_post_count_like, post.likeCount, post.likeCount)
            val commentString = resources.getQuantityString(R.plurals.class_post_count_comment, post.commentCount, post.commentCount)
            text = if (post.likeCount > 0 && post.commentCount > 0) {
                resources.getString(R.string.post_like_comments, likeString, commentString)
            } else if (post.likeCount <= 0 && post.commentCount > 0) {
                commentString
            } else if (post.likeCount > 0 && post.commentCount <= 0) {
                likeString
            } else {
                ""
            }
            visibility = if (text.isEmpty()) View.GONE else View.VISIBLE
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

        binding.tvLikeCommentCount.setOnClickListener {
            if (post.likeCount > 0) {
                onDisplayLikedUsersClicked(post.id)
            }
        }

        binding.tvReadCount.setOnClickListener {
            onDisplaySeenUsersClicked(post.id)
        }
    }
}