package whiz.sspark.library.view.widget.collaboration.class_activity.post.instructor

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.data.entity.PlatformOnlineClass
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.view.widget.collaboration.class_activity.online_class.online_class_container.instructor.InstructorOnlineClassContainerView

class InstructorClassPostAdapter(private val context: Context,
                                 private val items: List<Item>,
                                 private val allMemberCount: Int,
                                 private val color: Int,
                                 private val onDeletePostClicked: (Post) -> Unit,
                                 private val onEditPostClicked: (Post) -> Unit,
                                 private val onPostRead: (Any) -> Unit,
                                 private val onPostClicked: (Post) -> Unit,
                                 private val onImageClicked: (ImageView, Attachment) -> Unit,
                                 private val onLikeClicked: (Post) -> Unit,
                                 private val onCommentClicked: (Post) -> Unit,
                                 private val onFileClicked: (Attachment) -> Unit,
                                 private val onDisplayLikedUsersClicked: (Any) -> Unit,
                                 private val onDisplaySeenUsersClicked: (Any) -> Unit,
                                 private val onOnlineClassPlatformClicked: (String) -> Unit,
                                 private val onShowAllOnlineClassPlatformsClicked: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class InstructorClassPostViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class InstructorOnlineClassContainerViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    enum class ClassPostItemType(val type: Int) {
        POST(0),
        ONLINE_CLASSES(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ClassPostItemType.POST.type) {
            InstructorClassPostViewHolder(
                InstructorClassPostView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            )
        } else {
            InstructorOnlineClassContainerViewHolder(
                InstructorOnlineClassContainerView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.getOrNull(position) ?: Item()

        item.post?.let { post ->
            (holder.itemView as? InstructorClassPostView)?.apply {
                init(
                    post = post,
                    allMemberCount = allMemberCount,
                    color = color,
                    onDeletePostClicked = onDeletePostClicked,
                    onEditPostClicked = onEditPostClicked,
                    onPostRead = onPostRead,
                    onPostClicked = onPostClicked,
                    onImageClicked = onImageClicked,
                    onLikeClicked = onLikeClicked,
                    onCommentClicked = onCommentClicked,
                    onFileClicked = onFileClicked,
                    onDisplayLikedUsersClicked = onDisplayLikedUsersClicked,
                    onDisplaySeenUsersClicked = onDisplaySeenUsersClicked
                )

                setPadding(8.toDP(context), 0, 8.toDP(context), 4.toDP(context))
            }
        }

        item.onlineClasses?.let {
            (holder.itemView as? InstructorOnlineClassContainerView)?.apply {
                init(
                    onShowAllOnlineClassPlatformsClicked = onShowAllOnlineClassPlatformsClicked,
                    onOnlineClassPlatformClicked = onOnlineClassPlatformClicked
                )
                renderOnlineClasses(it)
                setPadding(8.toDP(context), 0, 0, 16.toDP(context))
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        val item = items.getOrNull(position)
        item?.let {
            return if (item.post != null) {
                ClassPostItemType.POST.type
            } else {
                ClassPostItemType.ONLINE_CLASSES.type
            }
        }

        return ClassPostItemType.POST.type
    }

    data class Item(
        val post: Post? = null,
        val onlineClasses: List<PlatformOnlineClass>? = null
    )
}