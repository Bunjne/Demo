package whiz.sspark.library.view.screen.collaboration.class_post_comment.student

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Comment
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.databinding.ViewClassPostCommentActivityBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showKeyboard
import whiz.sspark.library.utility.openFile
import whiz.sspark.library.view.general.custom_divider.CustomDividerItemDecoration
import whiz.sspark.library.view.widget.collaboration.class_post_comment.student.StudentClassPostCommentAdapter

class StudentClassPostCommentActivityView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassPostCommentActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(color: Int,
             postCommentItems: List<StudentClassPostCommentAdapter.PostCommentItem>,
             allMemberCount: Int,
             onImageClicked: (ImageView, String) -> Unit,
             onCommentItemClicked: (Comment) -> Unit,
             onPostLiked: (Post) -> Unit,
             onCommentSent: (String) -> Unit,
             onDisplayLikedUsersClicked: () -> Unit,
             onDisplaySeenUsersClicked: () -> Unit) {

        val linearLayoutManager = LinearLayoutManager(context)
        with(binding.rvPostComment) {
            layoutManager = linearLayoutManager

            if (itemDecorationCount == 0) {
                addItemDecoration(CustomDividerItemDecoration(
                    ContextCompat.getDrawable(context, R.drawable.divider_comment)!!,
                    StudentClassPostCommentAdapter.PostCommentType.COMMENT.type)
                )
            }

            adapter = StudentClassPostCommentAdapter(
                    context = context,
                    color = color,
                    items = postCommentItems,
                    allMemberCount = allMemberCount,
                    onImageClicked = { imageView, attachment -> onImageClicked(imageView, attachment.url) },
                    onFileClicked = { attachment -> openFile(context, attachment) },
                    onCommentClicked = {
                        val lastIndex = binding.rvPostComment.adapter?.itemCount ?: 1

                        scrollToPosition(lastIndex - 1)
                        showKeyboardMessageEdittext(true)
                    },
                    onCommentItemClicked = onCommentItemClicked,
                    onPostLiked = onPostLiked,
                    onDisplayLikedUsersClicked = onDisplayLikedUsersClicked,
                    onDisplaySeenUsersClicked = onDisplaySeenUsersClicked)
        }

        binding.ivSend.show(R.drawable.ic_send)
        binding.ivSend.setColorFilter(color)
        binding.ivSend.setOnClickListener {
            val enteredMessage = binding.etMessage.text.toString()
            if (enteredMessage.isNotBlank()) {
                onCommentSent(enteredMessage)

                binding.etMessage.text?.clear()
            }
        }

        binding.etMessage.setOnTouchListener { view, event ->
            if (binding.etMessage.hasFocus()) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }

        notifyCommentItemOnPost(postCommentItems.size)
    }

    private fun notifyCommentItemOnPost(size: Int) {
        if (size == 1) {
            binding.rvPostComment.adapter?.notifyItemInserted(0)
        } else {
            binding.rvPostComment.adapter?.notifyItemChanged(0)
        }
    }

    fun showKeyboardMessageEdittext(isKeyboardShown: Boolean) {
        if (isKeyboardShown) {
            Handler(Looper.getMainLooper()).postDelayed({
                val lastIndex = binding.rvPostComment.adapter?.itemCount ?: 1
                scrollToPosition(lastIndex - 1)
                binding.etMessage.showKeyboard()
            }, 200)
        }
    }

    fun scrollToPosition(position: Int) {
        binding.rvPostComment.smoothScrollToPosition(position)
    }

    fun notifyRecycleViewItemChanged(index: Int) {
        binding.rvPostComment.adapter?.notifyItemChanged(index)
    }

    fun notifyRecycleViewItemInserted(index: Int) {
        binding.rvPostComment.adapter?.notifyItemInserted(index)
    }

    fun updateItem() {
        binding.rvPostComment.adapter?.notifyDataSetChanged()
    }

    fun notifyRecycleViewItemRemoved(index: Int) {
        binding.rvPostComment.adapter?.notifyItemRemoved(index)
    }
}