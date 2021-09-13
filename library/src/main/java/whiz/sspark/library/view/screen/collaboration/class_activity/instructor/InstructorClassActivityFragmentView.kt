package whiz.sspark.library.view.screen.collaboration.class_activity.instructor

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.databinding.ViewInstructorClassActivityFragmentBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.utility.openFile
import whiz.sspark.library.utility.updateItem
import whiz.sspark.library.view.general.custom_divider.CustomDividerItemDecoration
import whiz.sspark.library.view.widget.collaboration.class_activity.post.instructor.InstructorClassPostAdapter

class InstructorClassActivityFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var items = mutableListOf<InstructorClassPostAdapter.Item>()

    private val binding by lazy {
        ViewInstructorClassActivityFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(allMemberCount: Int,
             color: Int,
             onRefresh: () -> Unit,
             onCreatePostClicked: () -> Unit,
             onDeletePostClicked: (Post) -> Unit = { },
             onEditPostClicked: (Post) -> Unit = { },
             onPostClicked: (Post, Boolean) -> Unit,
             onImageClicked: (ImageView, String) -> Unit,
             onLikeClicked: (Post) -> Unit,
             onCommentClicked: (Post, Boolean) -> Unit,
             onPostRead: (String) -> Unit,
             onShowAllOnlineClassPlatformsClicked: () -> Unit = { },
             onOnlineClassPlatformClicked: (String) -> Unit,
             onPostLikedUsersClicked:(String) -> Unit,
             onPostSeenUsersClicked:(String) -> Unit) {
        binding.ivActivity.show(R.drawable.ic_create_post)

        val linearLayoutManager = LinearLayoutManager(context)
        with(binding.rvPost) {
            layoutManager = linearLayoutManager

            addItemDecoration(CustomDividerItemDecoration(ContextCompat.getDrawable(context!!, R.drawable.divider_post)!!, InstructorClassPostAdapter.ClassPostItemType.POST.type))

            adapter = InstructorClassPostAdapter(
                    context = context!!,
                    items = items,
                    allMemberCount = allMemberCount,
                    color = color,
                    onDeletePostClicked = { post ->
                        onDeletePostClicked(post)
                    },
                    onEditPostClicked = { post ->
                        onEditPostClicked(post)
                    },
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
                    binding.rvPost.getGlobalVisibleRect(rvRect)

                    val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()

                    for (position in firstVisiblePosition..lastVisibleItemPosition) {
                        val itemRect = Rect()
                        linearLayoutManager.findViewByPosition(position)?.getGlobalVisibleRect(itemRect)

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

        binding.clCreatePost.setOnClickListener {
            onCreatePostClicked()
        }

        binding.srlContainer.setOnRefreshListener {
            onRefresh()
        }
    }

    fun refreshRecyclerView(items: List<InstructorClassPostAdapter.Item>) {
        binding.rvPost.adapter?.updateItem(this.items, items)

        checkPostAmount()
    }

    fun notifyRecycleViewItemChanged(index: Int) {
        binding.rvPost.adapter?.notifyItemChanged(index)
    }

        fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun scrollToTopPost() {
        if (items.any()) {
            binding.rvPost.layoutManager?.scrollToPosition(0)
        }
    }

    private fun checkPostAmount() {
        binding.tvNoPost.visibility = if (items.filter { it.post != null }.any()) View.INVISIBLE else View.VISIBLE
    }
}