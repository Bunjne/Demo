package whiz.sspark.library.view.widget.class_schedule

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.ClassScheduleCalendar
import whiz.sspark.library.data.entity.ClassScheduleCourse
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.CenterTextItemView
import whiz.sspark.library.view.widget.base.ItemListTitleView
import java.lang.IndexOutOfBoundsException

class ClassScheduleAdapter: ListAdapter<ClassScheduleAdapter.Item, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val TITLE_VIEW_TYPE = 2222
        const val CALENDAR_VIEW_TYPE = 3333
        const val COURSE_VIEW_TYPE = 4444
        const val NO_CLASS_VIEW_TYPE = 5555
    }

    private var recyclerViewHeight = 0

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_VIEW_TYPE
        }

        return when {
            item?.classScheduleCalendar != null -> CALENDAR_VIEW_TYPE
            item?.classScheduleCourse != null -> COURSE_VIEW_TYPE
            item?.noClassTitle != null -> NO_CLASS_VIEW_TYPE
            else -> TITLE_VIEW_TYPE
        }
    }

    private class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private class NoClassViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        recyclerViewHeight = parent.height

        return when(viewType) {
            CALENDAR_VIEW_TYPE -> CalendarViewHolder(ClassScheduleCalendarItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            COURSE_VIEW_TYPE -> CourseViewHolder(ClassScheduleCourseItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            NO_CLASS_VIEW_TYPE -> NoClassViewHolder(CenterTextItemView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            else -> TitleViewHolder(ItemListTitleView(parent.context).apply {
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
        val isNextItemHeader = getItemViewType(position + 1) != COURSE_VIEW_TYPE
        val isPreviousItemHeader = getItemViewType(position - 1) != COURSE_VIEW_TYPE

        when(viewType) {
            CALENDAR_VIEW_TYPE -> (holder.itemView as? ClassScheduleCalendarItemView)?.init(item.classScheduleCalendar!!)
            COURSE_VIEW_TYPE -> (holder.itemView as? ClassScheduleCourseItemView)?.apply {
                init(item.classScheduleCourse!!)

                setDarkModeBackground(
                    isNextItemHeader = isNextItemHeader,
                    isPreviousItemHeader = isPreviousItemHeader
                )
            }
            NO_CLASS_VIEW_TYPE -> (holder.itemView as? CenterTextItemView)?.apply {
                init(item.noClassTitle!!)

                post {
                    val lastItemBottom = holder.itemView.bottom
                    val lastItemHeight = holder.itemView.height
                    val heightDifference = recyclerViewHeight - lastItemBottom
                    val requiredHeight = lastItemHeight + heightDifference

                    if (requiredHeight != holder.itemView.height) {
                        holder.itemView.layoutParams.height = requiredHeight
                        notifyItemChanged(position)
                    }
                }
            }
            TITLE_VIEW_TYPE -> (holder.itemView as? ItemListTitleView)?.init(item.title!!)
        }
    }

    data class Item(
        val title: String? = null,
        val noClassTitle: String? = null,
        val classScheduleCalendar: ClassScheduleCalendar? = null,
        val classScheduleCourse: ClassScheduleCourse? = null
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