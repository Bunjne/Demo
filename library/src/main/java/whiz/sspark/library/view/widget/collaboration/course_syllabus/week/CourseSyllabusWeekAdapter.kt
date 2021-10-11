package whiz.sspark.library.view.widget.collaboration.course_syllabus.week

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.view.widget.collaboration.course_syllabus.CourseSyllabusTitleItemView
import java.lang.IndexOutOfBoundsException

class CourseSyllabusWeekAdapter: ListAdapter<CourseSyllabusWeekAdapter.Item, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        val TITLE_VIEW_TYPE = 1111
        val COURSE_DETAIL_TOP_VIEW_TYPE = 2222
        val COURSE_DETAIL_MIDDLE_VIEW_TYPE = 3333
        val COURSE_DETAIL_BOTTOM_VIEW_TYPE = 4444
        val COURSE_DETAIL_SINGLE_VIEW_TYPE = 5555
        val INTRUCTOR_VIEW_TYPE = 6666
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
            null
        }

        val previousItem = try {
            getItem(position - 1)
        } catch (e: IndexOutOfBoundsException) {
            null
        }

        return when {
            item.instructors != null -> INTRUCTOR_VIEW_TYPE
            item.courseDetail != null && previousItem?.courseDetail == null && nextItem?.courseDetail == null -> COURSE_DETAIL_SINGLE_VIEW_TYPE
            item.courseDetail != null && previousItem?.courseDetail != null && nextItem?.courseDetail != null -> COURSE_DETAIL_MIDDLE_VIEW_TYPE
            item.courseDetail != null && previousItem?.courseDetail == null && nextItem?.courseDetail != null -> COURSE_DETAIL_TOP_VIEW_TYPE
            item.courseDetail != null && previousItem?.courseDetail != null && nextItem?.courseDetail == null -> COURSE_DETAIL_BOTTOM_VIEW_TYPE
            else -> TITLE_VIEW_TYPE
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseDetailTopViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseDetailMiddleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseDetailBottomViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseDetailSingleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class InstructorViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            COURSE_DETAIL_TOP_VIEW_TYPE -> CourseDetailTopViewHolder(CourseSyllabusWeekDetailTopItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            COURSE_DETAIL_MIDDLE_VIEW_TYPE -> CourseDetailMiddleViewHolder(CourseSyllabusWeekDetailMiddleItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            COURSE_DETAIL_BOTTOM_VIEW_TYPE -> CourseDetailBottomViewHolder(CourseSyllabusWeekDetailBottomItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            COURSE_DETAIL_SINGLE_VIEW_TYPE -> CourseDetailSingleViewHolder(CourseSyllabusWeekDetailSingleItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            INTRUCTOR_VIEW_TYPE -> InstructorViewHolder(ViewCourseSyllabusInstructorItem(parent.context).apply {
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
            COURSE_DETAIL_TOP_VIEW_TYPE -> (holder.itemView as CourseSyllabusWeekDetailTopItemView).init(item.courseDetail!!)
            COURSE_DETAIL_MIDDLE_VIEW_TYPE -> (holder.itemView as CourseSyllabusWeekDetailMiddleItemView).init(item.courseDetail!!)
            COURSE_DETAIL_BOTTOM_VIEW_TYPE -> (holder.itemView as CourseSyllabusWeekDetailBottomItemView).init(item.courseDetail!!)
            COURSE_DETAIL_SINGLE_VIEW_TYPE -> (holder.itemView as CourseSyllabusWeekDetailSingleItemView).init(item.courseDetail!!)
            INTRUCTOR_VIEW_TYPE -> (holder.itemView as ViewCourseSyllabusInstructorItem).init(item.instructors!!)
            TITLE_VIEW_TYPE -> (holder.itemView as CourseSyllabusTitleItemView).init(item.title!!)
        }
    }

    data class Item(
        val title: String? = null,
        val courseDetail: String? = null,
        val instructors: List<String>? = null
    )

    private class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            val sameInstructor = if (oldItem.instructors != null && newItem.instructors != null) {
                oldItem.instructors.containsAll(newItem.instructors) && newItem.instructors.containsAll(oldItem.instructors)
            } else {
                !(oldItem.instructors != null || newItem.instructors != null)
            }

            return oldItem == newItem && sameInstructor
        }
    }
}