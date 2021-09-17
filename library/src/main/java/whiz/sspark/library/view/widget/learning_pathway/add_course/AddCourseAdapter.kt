package whiz.sspark.library.view.widget.learning_pathway.add_course

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.ItemListTitleView

class AddCourseAdapter(private val onAddCourseClicked: (String) -> Unit): ListAdapter<AddCourseAdapter.Item, RecyclerView.ViewHolder>(AddCourseDiffCallback()) {

    companion object {
        const val TITLE_VIEW_TYPE = 1111
        const val SELECTABLE_COURSE_VIEW_TYPE = 2222
        const val UN_SELECTABLE_COURSE_VIEW_TYPE = 3333
    }


    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            null
        }

        return when {
            item?.course != null && item.isSelectable -> SELECTABLE_COURSE_VIEW_TYPE
            item?.course != null && !item.isSelectable -> UN_SELECTABLE_COURSE_VIEW_TYPE
            else -> TITLE_VIEW_TYPE
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class SelectableCourseViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class UnSelectableCourseViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SELECTABLE_COURSE_VIEW_TYPE -> {
                SelectableCourseViewHolder(CourseItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            UN_SELECTABLE_COURSE_VIEW_TYPE -> {
                UnSelectableCourseViewHolder(SelectedCourseItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            else -> {
                TitleViewHolder(ItemListTitleView(parent.context).apply {
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
        val isNextItemHeader = getItemViewType(position + 1) == TITLE_VIEW_TYPE
        val isPreviousItemHeader = getItemViewType(position - 1) == TITLE_VIEW_TYPE

        when(viewType) {
            SELECTABLE_COURSE_VIEW_TYPE -> {
                (holder.itemView as? CourseItemView)?.apply {
                    init(item.course!!)

                    setOnClickListener {
                        onAddCourseClicked(item.course.id)
                    }

                    setDarkModeBackground(isNextItemHeader, isPreviousItemHeader)
                }
            }
            UN_SELECTABLE_COURSE_VIEW_TYPE -> {
                (holder.itemView as? SelectedCourseItemView)?.apply {
                    init(item.course!!)
                    setDarkModeBackground(isNextItemHeader, isPreviousItemHeader)
                }
            }
            else -> {
                (holder.itemView as? ItemListTitleView)?.init(item.title!!)
            }
        }
    }

    data class Item(
        val title: String? = null,
        val course: Course? = null,
        val isSelectable: Boolean = false
    )

    private class AddCourseDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.course == newItem.course
        }
    }
}