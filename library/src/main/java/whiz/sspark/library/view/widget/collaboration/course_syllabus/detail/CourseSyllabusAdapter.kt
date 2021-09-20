package whiz.sspark.library.view.widget.collaboration.course_syllabus.detail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.view.widget.collaboration.course_syllabus.CourseSyllabusTitleItemView
import java.lang.IndexOutOfBoundsException

class CourseSyllabusAdapter: ListAdapter<CourseSyllabusAdapter.Item, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        val TITLE_VIEW_TYPE = 1111
        val COURSE_DETAIL_TOP_VIEW_TYPE = 2222
        val COURSE_DETAIL_MIDDLE_VIEW_TYPE = 3333
        val COURSE_DETAIL_BOTTOM_VIEW_TYPE = 4444
        val COURSE_DETAIL_SINGLE_VIEW_TYPE = 5555
    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_VIEW_TYPE
        }

        val nextItem = try {
            getItem(position + 1)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_VIEW_TYPE
        }

        val previousItem = try {
            getItem(position - 1)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_VIEW_TYPE
        }

        return when {
            item.courseDetail != null && previousItem.courseDetail == null && nextItem.courseDetail == null -> COURSE_DETAIL_SINGLE_VIEW_TYPE
            item.courseDetail != null && previousItem.courseDetail != null && nextItem.courseDetail != null -> COURSE_DETAIL_MIDDLE_VIEW_TYPE
            item.courseDetail != null && previousItem.courseDetail == null && nextItem.courseDetail != null -> COURSE_DETAIL_TOP_VIEW_TYPE
            item.courseDetail != null && previousItem.courseDetail != null && nextItem.courseDetail == null -> COURSE_DETAIL_BOTTOM_VIEW_TYPE
            else -> TITLE_VIEW_TYPE
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseDetailTopViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseDetailMiddleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseDetailBottomViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseDetailSingleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            COURSE_DETAIL_TOP_VIEW_TYPE -> CourseDetailTopViewHolder(CourseSyllabusDetailTopItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            COURSE_DETAIL_MIDDLE_VIEW_TYPE -> CourseDetailMiddleViewHolder(CourseSyllabusDetailMiddleItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            COURSE_DETAIL_BOTTOM_VIEW_TYPE -> CourseDetailBottomViewHolder(CourseSyllabusDetailBottomItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            COURSE_DETAIL_SINGLE_VIEW_TYPE -> CourseDetailSingleViewHolder(CourseSyllabusDetailSingleItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            else -> TitleViewHolder(CourseSyllabusTitleItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val viewType = getItemViewType(position)

        when(viewType) {
            COURSE_DETAIL_TOP_VIEW_TYPE -> (holder.itemView as CourseSyllabusDetailTopItemView).init(item.position, item.courseDetail!!)
            COURSE_DETAIL_MIDDLE_VIEW_TYPE -> (holder.itemView as CourseSyllabusDetailMiddleItemView).init(item.position, item.courseDetail!!)
            COURSE_DETAIL_BOTTOM_VIEW_TYPE -> (holder.itemView as CourseSyllabusDetailBottomItemView).init(item.position, item.courseDetail!!)
            COURSE_DETAIL_SINGLE_VIEW_TYPE -> (holder.itemView as CourseSyllabusDetailSingleItemView).init(item.position, item.courseDetail!!)
            TITLE_VIEW_TYPE -> (holder.itemView as CourseSyllabusTitleItemView).init(item.title!!)
        }
    }

    data class Item(
        val title: String? = null,
        val position: Int? = null,
        val courseDetail: String? = null,
    )

    private class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}