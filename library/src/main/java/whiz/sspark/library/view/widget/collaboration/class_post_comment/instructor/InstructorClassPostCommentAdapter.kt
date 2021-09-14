package whiz.sspark.library.view.widget.collaboration.class_post_comment.instructor

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.view.widget.collaboration.class_activity.post.instructor.InstructorClassPostView
import whiz.sspark.library.view.widget.collaboration.class_post_comment.ClassPostCommentView

class InstructorClassPostCommentAdapter(private val context: Context,
                                        private val color: Int,
                                        private val items: List<PostCommentItem>,
                                        private val allMemberCount: Int,
                                        private val onDeletePostClicked: (Post) -> Unit = {},
                                        private val onEditPostClicked: (Post) -> Unit = {},
                                        private val onImageClicked: (ImageView, Attachment) -> Unit,
                                        private val onFileClicked: (Attachment) -> Unit,
                                        private val onCommentClicked: () -> Unit,
                                        private val onCommentItemClicked: (Post) -> Unit,
                                        private val onPostLiked: (Post) -> Unit,
                                        private val onDisplayLikedUsersClicked :() -> Unit,
                                        private val onDisplaySeenUsersClicked :() -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class InstructorClassPostViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class ClassPostCommentViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    enum class PostCommentType(val type: Int) {
        POST(1),
        COMMENT(2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PostCommentType.POST.type -> InstructorClassPostViewHolder(
                InstructorClassPostView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            else -> ClassPostCommentViewHolder(
                ClassPostCommentView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.getOrNull(position)

        item?.let { item ->
            if (item.type == PostCommentType.POST) {
                val post = item.post
                (holder.itemView as InstructorClassPostView).init(
                    post = post,
                    allMemberCount = allMemberCount,
                    color = color,
                    onDeletePostClicked = { post -> onDeletePostClicked(post) },
                    onEditPostClicked = { post -> onEditPostClicked(post) },
                    onImageClicked = { imageView, attachment -> onImageClicked(imageView, attachment) },
                    onFileClicked = { attachment -> onFileClicked(attachment) },
                    onLikeClicked = { post -> onPostLiked(post) },
                    onCommentClicked = { onCommentClicked() },
                    onDisplayLikedUsersClicked = { onDisplayLikedUsersClicked() },
                    onDisplaySeenUsersClicked = { onDisplaySeenUsersClicked() })
            } else {
                val comment = item.post
                (holder.itemView as ClassPostCommentView).apply {
                    init(comment, color, onCommentItemClicked)

                    val isFirstComment = position == 1

                    if (isFirstComment) {
                        setPadding(0, 16.toDP(context), 0, 0)
                    } else {
                        setPadding(0, 0, 0, 0)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int) = if (items[position].type == PostCommentType.POST) {
        PostCommentType.POST.type
    } else {
        PostCommentType.COMMENT.type
    }

    override fun getItemCount() = items.size

    data class PostCommentItem(
        val type: PostCommentType,
        val post: Post
    )
}