package whiz.sspark.library.view.widget.expect_outcome

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.InstructorCommentItem
import whiz.sspark.library.data.entity.ExpectOutcomeCourseItem
import whiz.sspark.library.data.entity.ExpectOutcomeOverallItem
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.InstructorCommentView
import whiz.sspark.library.view.widget.base.ItemListTitleView
import java.lang.IndexOutOfBoundsException

class ExpectOutcomeAdapter(private val context: Context): ListAdapter<ExpectOutcomeAdapter.Item, RecyclerView.ViewHolder>(ExpectOutcomeDiffCallback()) {

    companion object {
        const val TITLE_TYPE = 1111
        const val COMMENT_TYPE = 2222
        const val COURSE_TYPE = 3333
        const val OVERALL_TYPE = 4444
    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_TYPE
        }

        return when {
            item.courseItem != null -> COURSE_TYPE
            item.commentItem != null -> COMMENT_TYPE
            item.overAllItem != null -> OVERALL_TYPE
            else -> TITLE_TYPE
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CommentViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class OverallViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            COMMENT_TYPE -> {
                CommentViewHolder(InstructorCommentView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            COURSE_TYPE -> {
                CourseViewHolder(ExpectOutcomeItemView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            OVERALL_TYPE -> {
                OverallViewHolder(ExpectOutcomeOverAllItemView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            else -> {
                TitleViewHolder(ItemListTitleView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val viewType = getItemViewType(position)
        val isNextItemHeader = getItemViewType(position + 1) != COURSE_TYPE
        val isPreviousItemHeader = getItemViewType(position - 1) != COURSE_TYPE

        when (viewType) {
            COMMENT_TYPE -> {
                (holder.itemView as? InstructorCommentView)?.init(item.commentItem!!)
            }
            COURSE_TYPE -> {
                (holder.itemView as? ExpectOutcomeItemView)?.apply {
                    init(item.courseItem!!)
                    setDarkModeBackground(isNextItemHeader, isPreviousItemHeader)
                }
            }
            OVERALL_TYPE -> {
                (holder.itemView as? ExpectOutcomeOverAllItemView)?.init(item.overAllItem!!)
            }
            TITLE_TYPE -> {
                (holder.itemView as? ItemListTitleView)?.init(item.title!!)
            }
        }
    }

    data class Item(
        val title: String? = null,
        val commentItem: InstructorCommentItem? = null,
        val overAllItem: ExpectOutcomeOverallItem? = null,
        val courseItem: ExpectOutcomeCourseItem? = null
    )
}

private class ExpectOutcomeDiffCallback : DiffUtil.ItemCallback<ExpectOutcomeAdapter.Item>() {
    override fun areItemsTheSame(oldItem: ExpectOutcomeAdapter.Item, newItem: ExpectOutcomeAdapter.Item): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: ExpectOutcomeAdapter.Item, newItem: ExpectOutcomeAdapter.Item): Boolean {
        return oldItem == newItem
    }
}