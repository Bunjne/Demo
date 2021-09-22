package whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.ItemListTitleView

class LikeBySeenByItemAdapter(private val context: Context,
                              private val items: List<LikeBySeenByAdapterViewType>): RecyclerView.Adapter<LikeBySeenByItemAdapter.ViewHolder>() {

    companion object {
        const val TITLE_TYPE = 1
        const val STUDENT_TYPE = 2
        const val INSTRUCTOR_TYPE = 3
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(aparent: ViewGroup, viewType: Int): ViewHolder {
        val layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

        return when (viewType) {
            TITLE_TYPE -> ViewHolder(ItemListTitleView(context).apply {
                this.layoutParams = layoutParams
            })
            STUDENT_TYPE -> ViewHolder(LikeBySeenByStudentItemView(context).apply {
                this.layoutParams = layoutParams
            })
            else -> ViewHolder(LikeBySeenByInstructorItemView(context).apply {
                this.layoutParams = layoutParams
            })
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        val isNextItemTitle = getItemViewType(position + 1) == TITLE_TYPE
        val isPreviousItemTitle = getItemViewType(position - 1) == TITLE_TYPE

        item?.let {
            with (holder.itemView) {
                when {
                    this is ItemListTitleView && item is LikeBySeenByAdapterViewType.Header -> {
                        init(item.title)
                    }
                    this is LikeBySeenByStudentItemView && item is LikeBySeenByAdapterViewType.Student -> {
                        init(item.student, item.rank)

                        setDarkModeBackground(
                            isNextItemHeader = isNextItemTitle,
                            isPreviousItemHeader = isPreviousItemTitle
                        )
                    }
                    this is LikeBySeenByInstructorItemView && item is LikeBySeenByAdapterViewType.Instructor -> {
                        init(item.instructor)
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
            is LikeBySeenByAdapterViewType.Header -> TITLE_TYPE
            is LikeBySeenByAdapterViewType.Student -> STUDENT_TYPE
            is LikeBySeenByAdapterViewType.Instructor -> INSTRUCTOR_TYPE
            else -> TITLE_TYPE
        }
    }

    sealed class LikeBySeenByAdapterViewType {
        data class Header(val title: String): LikeBySeenByAdapterViewType()
        data class Student(val student: ClassMember, val rank: Int): LikeBySeenByAdapterViewType()
        data class Instructor(val instructor: ClassMember): LikeBySeenByAdapterViewType()
    }
}