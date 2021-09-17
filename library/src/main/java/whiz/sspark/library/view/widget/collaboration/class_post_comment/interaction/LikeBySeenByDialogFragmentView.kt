package whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.data.entity.Member
import whiz.sspark.library.data.enum.PostInteraction
import whiz.sspark.library.databinding.ViewLikeBySeenByBinding
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.view.general.custom_divider.CustomDividerItemDecoration
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration

class LikeBySeenByDialogFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLikeBySeenByBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val items = mutableListOf<LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType>()

    fun init(startColor: Int,
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
                dividerViewType = listOf(R.layout.view_like_by_seen_by_student_item, R.layout.view_like_by_seen_by_instructor_item))
            )

            adapter = LikeBySeenByItemAdapter(
                context = context,
                items = items
            )
        }
    }

    fun renderMembers(member: Member, interactedMemberIds: List<String>) {
        val instructors = member.instructors.filter { interactedMemberIds.contains(it.userId) }
        val students = member.students.filter { interactedMemberIds.contains(it.userId) }

        with(items) {
            clear()

            if (instructors.isNotEmpty()) {
                add(
                    LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Header(
                    title = resources.getString(R.string.like_by_seen_by_instructor_title, instructors.size)
                    )
                )

                addAll(
                    instructors.map { instructor ->
                    LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Instructor(
                        instructor = instructor)
                    }
                )
            }

            if (students.isNotEmpty()) {
                add(
                    LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Header(
                    title = resources.getString(R.string.like_by_seen_by_student_title, students.size)
                    )
                )

                addAll(
                    students.mapIndexed { index, student ->
                        LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Student(
                            student = student,
                            rank = index)
                    }
                )
            }
        }

        binding.rvMemberLikedOrSeen.adapter?.notifyDataSetChanged()
    }

    fun setSwipeRefreshLoading(isLoading: Boolean) {
        binding.srlMember.isRefreshing = isLoading
    }
}