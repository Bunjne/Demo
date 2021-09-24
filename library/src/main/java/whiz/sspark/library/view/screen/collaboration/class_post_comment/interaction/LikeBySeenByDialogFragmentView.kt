package whiz.sspark.library.view.screen.collaboration.class_post_comment.interaction

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.enum.PostInteraction
import whiz.sspark.library.databinding.ViewLikeBySeenByDialogFragmentBinding
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.utility.updateItem
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction.LikeBySeenByItemAdapter

class LikeBySeenByDialogFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLikeBySeenByDialogFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(matchedMembers: MutableList<LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType>,
             startColor: Int,
             endColor: Int,
             postInteractionType: Int,
             onCloseClicked: () -> Unit,
             onRefresh: () -> Unit) {
        val radius = 12f.toDP(context)
        val gradientDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
            colors = intArrayOf(startColor, endColor)
            cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
        }

        binding.srlMember.setOnRefreshListener {
            onRefresh()
        }

        binding.tvClose.setOnClickListener {
            onCloseClicked()
        }

        binding.tvTitle.background = gradientDrawable
        binding.tvTitle.text = when(postInteractionType) {
            PostInteraction.LIKE.type -> resources.getString(R.string.like_by_seen_by_liked_title)
            PostInteraction.SEEN.type -> resources.getString(R.string.like_by_seen_by_seen_title)
            else -> ""
        }

        with(binding.rvMemberLikedOrSeen) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(CustomDividerMultiItemDecoration(
                divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                dividerViewType = listOf(
                    LikeBySeenByItemAdapter.INSTRUCTOR_TYPE,
                    LikeBySeenByItemAdapter.STUDENT_TYPE
                ))
            )

            adapter = LikeBySeenByItemAdapter(
                context = context,
                items = matchedMembers
            )
        }
    }

    fun renderMembers(members: MutableList<LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType>, updatedMembers: List<LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType>) {
        binding.rvMemberLikedOrSeen.adapter?.updateItem(members, updatedMembers)
    }

    fun setSwipeRefreshLoading(isLoading: Boolean) {
        binding.srlMember.isRefreshing = isLoading
    }
}