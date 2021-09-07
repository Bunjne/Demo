package whiz.sspark.library.view.widget.collaboration.class_activity.post

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.data.entity.PlatformOnlineClass
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.view.widget.collaboration.class_activity.online_class.OnlineClassContainerView

class ClassPostAdapter(private val context: Context,
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
                       private val onShowAllOnlineClassPlatformsClicked: () -> Unit) : RecyclerView.Adapter<ClassPostAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    enum class ClassPostItemType(val type: Int) {
        POST(0),
        ONLINE_CLASSES(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ClassPostItemType.POST.type) {
            ViewHolder(
                ClassPostView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            )
        } else {
            ViewHolder(
                OnlineClassContainerView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getOrNull(position) ?: Item()

        item.post?.let { post ->
            (holder.itemView as? ClassPostView)?.init(
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
                onDisplaySeenUsersClicked = onDisplaySeenUsersClicked)
        }

        item.onlineClasses?.let {
            (holder.itemView as? OnlineClassContainerView)?.apply {
                init(
                    onShowAllOnlineClassPlatformsClicked = onShowAllOnlineClassPlatformsClicked,
                    onOnlineClassPlatformClicked = onOnlineClassPlatformClicked
                )
                renderOnlineClasses(it)
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