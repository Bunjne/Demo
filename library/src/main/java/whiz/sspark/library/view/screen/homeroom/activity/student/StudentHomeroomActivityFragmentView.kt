package whiz.sspark.library.view.screen.homeroom.activity.student

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.databinding.ViewStudentClassActivityFragmentBinding
import whiz.sspark.library.utility.openFile
import whiz.sspark.library.utility.updateItem
import whiz.sspark.library.view.widget.collaboration.class_activity.post.student.StudentClassPostAdapter

class StudentHomeroomActivityFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var items = mutableListOf<StudentClassPostAdapter.Item>()

    private val binding by lazy {
        ViewStudentClassActivityFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(allMemberCount: Int,
             color: Int,
             onRefresh: () -> Unit,
             onPostClicked: (Post, Boolean) -> Unit,
             onImageClicked: (ImageView, String) -> Unit,
             onLikeClicked: (Post) -> Unit,
             onCommentClicked: (Post, Boolean) -> Unit,
             onPostRead: (String) -> Unit,
             onOnlineClassPlatformClicked: (String) -> Unit,
             onPostLikedUsersClicked:(String) -> Unit,
             onPostSeenUsersClicked:(String) -> Unit) {

        val linearLayoutManager = LinearLayoutManager(context)
        with(binding.rvPost) {
            layoutManager = linearLayoutManager

            adapter = StudentClassPostAdapter(
                    context = context!!,
                    items = items,
                    allMemberCount = allMemberCount,
                    color = color,
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

        binding.srlContainer.setOnRefreshListener {
            onRefresh()
        }
    }

    fun refreshRecyclerView(items: List<StudentClassPostAdapter.Item>) {
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