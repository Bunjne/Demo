package whiz.sspark.library.view.screen.class_detail.class_activity

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_class_activity_fragment.view.*
import whiz.u.library.R
import whiz.u.library.USparkLibrary
import whiz.u.library.data.entity.OnlineClass
import whiz.u.library.data.entity.PlatformOnlineClass
import whiz.u.library.data.entity.Post
import whiz.u.library.extension.inflate
import whiz.u.library.extension.show
import whiz.u.library.utility.openFile
import whiz.u.library.widget.collaboration.detail.post.ClassPostAdapter

class ClassActivityFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var items = mutableListOf<ClassPostAdapter.Item>()

    init {
        inflate(R.layout.view_class_activity_fragment)
    }

    fun init(items: List<ClassPostAdapter.Item>,
             allMemberCount: Int,
             color: Int,
             onRefresh: () -> Unit,
             onDeletePostClicked: (Post) -> Unit = { },
             onEditPostClicked: (Post) -> Unit = { },
             onPostClicked: (Post, Boolean) -> Unit,
             onImageClicked: (ImageView, String) -> Unit,
             onLikeClicked: (Post) -> Unit,
             onCommentClicked: (Post, Boolean) -> Unit,
             onPostRead: (Any) -> Unit,
             onShowAllOnlineClassPlatformsClicked: () -> Unit,
             onOnlineClassPlatformClicked: (String) -> Unit,
             onPostLikedUsersClicked:(Any) -> Unit,
             onPostSeenUsersClicked:(Any) -> Unit) {

        with(this.items) {
            clear()
            addAll(items)
        }

        ivActivity.show(R.drawable.ic_create_post)

        val linearLayoutManager = LinearLayoutManager(context)
        with(rvPost) {
            layoutManager = linearLayoutManager

            addItemDecoration(DividerItemDecoration(context!!, linearLayoutManager.orientation).apply {
                setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider_post)!!)
            })

            adapter = ClassPostAdapter(
                    context = context!!,
                    items = this@ClassActivityFragmentView.items,
                    allMemberCount = allMemberCount,
                    color = color,
                    onDeletePostClicked = { post ->
                        onDeletePostClicked(post)
                    },
                    onEditPostClicked = { post ->
                        onEditPostClicked(post)
                    },
                    onPostRead = { id -> },
                    onPostClicked = { post ->
                        onPostClicked(post, false)
                    },
                    onImageClicked = { ivPost, attachmentImage ->
                        onImageClicked(ivPost, attachmentImage.url)
                    },
                    onLikeClicked = { post ->
                        onLikeClicked(post)
                    },
                    onCommentClicked = { post ->
                        onCommentClicked(post, true)
                    },
                    onFileClicked = { attachmentFile ->
                        openFile(context!!, attachmentFile)
                    },
                    onDisplayLikedUsersClicked = { postId ->
                        onPostLikedUsersClicked(postId)
                    },
                    onDisplaySeenUsersClicked = { postId ->
                        onPostSeenUsersClicked(postId)
                    },
                    onShowAllOnlineClassPlatformsClicked = onShowAllOnlineClassPlatformsClicked,
                    onOnlineClassPlatformClicked = onOnlineClassPlatformClicked
                )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val rvRect = Rect()
                    rvPost.getGlobalVisibleRect(rvRect)

                    val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()

                    for (position in firstVisiblePosition..lastVisibleItemPosition) {
                        val itemRect = Rect()
                        linearLayoutManager.findViewByPosition(position)
                            ?.getGlobalVisibleRect(itemRect)

                        val post = items.getOrNull(position)?.post

                        post?.let { post ->
                            if (itemRect.bottom <= rvRect.bottom && !post.isRead) {
                                onPostRead(post.id)
                            }
                        }
                    }
                }
            })
        }

        srlContainer.setOnRefreshListener {
            onRefresh()
        }

        if (USparkLibrary.isStudent) {
            clCreatePost.visibility = View.GONE
        }
    }

    fun setOnCreatePostClicked(onCreatePostClicked: () -> Unit) {
        clCreatePost.setOnClickListener {
            onCreatePostClicked()
        }
    }

    fun refreshRecyclerView(items: List<ClassPostAdapter.Item>) {
        with(this.items) {
            clear()
            addAll(items)
        }

        rvPost.adapter?.notifyDataSetChanged()
    }

    fun notifyRecycleViewItemChanged(index: Int) {
        rvPost.adapter?.notifyItemChanged(index)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        srlContainer.isRefreshing = isLoading == true
    }

    fun checkPostAmount() {
        tvNoPost.visibility = if (items.filter { it.post != null }.any()) View.INVISIBLE else View.VISIBLE
    }

    fun scrollToTopPost() {
        if (items.any()) {
            rvPost.layoutManager?.scrollToPosition(0)
        }
    }
}