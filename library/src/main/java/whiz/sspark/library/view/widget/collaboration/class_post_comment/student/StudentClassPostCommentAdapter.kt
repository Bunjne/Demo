package whiz.sspark.library.view.widget.collaboration.class_post_comment.student

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.view.widget.collaboration.class_activity.post.student.StudentClassPostView
import whiz.sspark.library.view.widget.collaboration.class_post_comment.ClassPostCommentView

class StudentClassPostCommentAdapter(private val context: Context,
                                     private val color: Int,
                                     private val items: List<PostCommentItem>,
                                     private val allMemberCount: Int,
                                     private val onImageClicked: (ImageView, Attachment) -> Unit,
                                     private val onFileClicked: (Attachment) -> Unit,
                                     private val onCommentClicked: () -> Unit,
                                     private val onCommentItemClicked: (Post) -> Unit,
                                     private val onPostLiked: (Post) -> Unit,
                                     private val onDisplayLikedUsersClicked :() -> Unit,
                                     private val onDisplaySeenUsersClicked :() -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class StudentClassPostViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class ClassPostCommentViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    enum class PostCommentType(val type: Int) {
        POST(1),
        COMMENT(2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PostCommentType.POST.type -> StudentClassPostViewHolder(
                StudentClassPostView(context).apply {
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
                (holder.itemView as StudentClassPostView).init(
                    post = post,
                    allMemberCount = allMemberCount,
                    color = color,
                    onImageClicked = onImageClicked,
                    onFileClicked = onFileClicked,
                    onLikeClicked = onPostLiked,
                    onCommentClicked = { onCommentClicked() },
                    onDisplayLikedUsersClicked = { onDisplayLikedUsersClicked() },
                    onDisplaySeenUsersClicked = { onDisplaySeenUsersClicked() })
            } else {
                val comment = item.post
                (holder.itemView as ClassPostCommentView).init(comment, color, onCommentItemClicked)
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