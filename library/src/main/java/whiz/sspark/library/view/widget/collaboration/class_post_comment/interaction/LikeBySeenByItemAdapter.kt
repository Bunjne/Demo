package whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.utility.getItemPositionType

class LikeBySeenByItemAdapter(private val context: Context,
                              private val items: List<LikeBySeenByAdapterViewType>): RecyclerView.Adapter<LikeBySeenByItemAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

        return when (viewType) {
            R.layout.view_like_by_seen_by_title_item -> ViewHolder(LikeBySeenByTitleItemView(context).apply {
                this.layoutParams = layoutParams
            })
            R.layout.view_like_by_seen_by_student_item -> ViewHolder(LikeBySeenByStudentItemVIew(context).apply {
                this.layoutParams = layoutParams
            })
            else -> ViewHolder(LikeBySeenByInstructorItemView(context).apply {
                this.layoutParams = layoutParams
            })
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        val isNextItemTitle = getItemViewType(position + 1) == R.layout.view_like_by_seen_by_title_item
        val isPreviousItemTitle = getItemViewType(position - 1) == R.layout.view_like_by_seen_by_title_item

        item?.let {
            with (holder.itemView) {
                when {
                    this is LikeBySeenByTitleItemView && item is LikeBySeenByAdapterViewType.Header -> {
                        init(item)
                    }
                    this is LikeBySeenByStudentItemVIew && item is LikeBySeenByAdapterViewType.Student -> {
                        init(item)

                        setDarkModeBackground(
                            isNextItemHeader = isNextItemTitle,
                            isPreviousItemHeader = isPreviousItemTitle
                        )
                    }
                    this is LikeBySeenByInstructorItemView && item is LikeBySeenByAdapterViewType.Instructor -> {
                        init(item)
                        setDarkModeBackground(
                            isNextItemHeader = isNextItemTitle,
                            isPreviousItemHeader = isPreviousItemTitle
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items.getOrNull(position)) {
            is LikeBySeenByAdapterViewType.Header -> R.layout.view_like_by_seen_by_title_item
            is LikeBySeenByAdapterViewType.Student -> R.layout.view_like_by_seen_by_student_item
            is LikeBySeenByAdapterViewType.Instructor -> R.layout.view_like_by_seen_by_instructor_item
            else -> R.layout.view_like_by_seen_by_title_item
        }
    }

    sealed class LikeBySeenByAdapterViewType {
        class Header(val title: String): LikeBySeenByAdapterViewType()
        class Student(val student: ClassMember, val rank: Int): LikeBySeenByAdapterViewType()
        class Instructor(val instructor: ClassMember): LikeBySeenByAdapterViewType()
    }
}