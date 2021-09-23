package whiz.sspark.library.view.widget.learning_pathway

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.*

class LearningPathwayAdapter(private val onAddCourseClicked: (Term, Int, Int, Int, List<String>) -> Unit,
                             private val onDeleteCourseClicked: (String, String) -> Unit,
                             private val onShowRequiredCourseClicked: (Term, List<Course>) -> Unit): ListAdapter<LearningPathwayAdapter.Item, RecyclerView.ViewHolder>(LearningPathwayDiffCallback()) {

    companion object {
        const val HEADER_VIEW_TYPE = 1111
        const val COURSE_TOP_VIEW_TYPE = 2222
        const val COURSE_MIDDLE_VIEW_TYPE = 3333
        const val COURSE_BOTTOM_VIEW_TYPE = 4444
        const val COURSE_SINGLE_VIEW_TYPE = 5555
        const val REQUIRED_COURSE_VIEW_TYPE = 6666

        val courseViewTypes = listOf(COURSE_TOP_VIEW_TYPE, COURSE_MIDDLE_VIEW_TYPE, COURSE_BOTTOM_VIEW_TYPE, COURSE_SINGLE_VIEW_TYPE)
    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            null
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
            item?.requiredCourseItem != null -> {
                REQUIRED_COURSE_VIEW_TYPE
            }
            item?.courseItem != null && previousItem?.courseItem == null && nextItem?.courseItem != null -> {
                COURSE_TOP_VIEW_TYPE
            }
            item?.courseItem != null && previousItem?.courseItem != null && nextItem?.courseItem != null -> {
                COURSE_MIDDLE_VIEW_TYPE
            }
            item?.courseItem != null && previousItem?.courseItem != null && nextItem?.courseItem == null -> {
                COURSE_BOTTOM_VIEW_TYPE
            }
            item?.courseItem != null && previousItem?.courseItem == null && nextItem?.courseItem == null -> {
                COURSE_SINGLE_VIEW_TYPE
            }
            else -> {
                HEADER_VIEW_TYPE
            }
        }
    }

    class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseTopViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseMiddleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseBottomViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseSingleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class RequiredCourseViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            COURSE_TOP_VIEW_TYPE -> {
                CourseTopViewHolder(LearningPathwayCourseTopItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            COURSE_MIDDLE_VIEW_TYPE -> {
                CourseMiddleViewHolder(LearningPathwayCourseMiddleItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            COURSE_BOTTOM_VIEW_TYPE -> {
                CourseBottomViewHolder(LearningPathwayCourseBottomItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            COURSE_SINGLE_VIEW_TYPE -> {
                CourseSingleViewHolder(LearningPathwayCourseSingleItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            REQUIRED_COURSE_VIEW_TYPE -> {
                RequiredCourseViewHolder(LearningPathwayRequiredCourseItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            else -> {
                HeaderViewHolder(LearningPathwayHeaderItemView(parent.context).apply {
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

        when(viewType) {
            COURSE_TOP_VIEW_TYPE -> {
                (holder.itemView as? LearningPathwayCourseTopItemView)?.init(item.courseItem!!, onDeleteCourseClicked)
            }
            COURSE_MIDDLE_VIEW_TYPE -> {
                (holder.itemView as? LearningPathwayCourseMiddleItemView)?.init(item.courseItem!!, onDeleteCourseClicked)
            }
            COURSE_BOTTOM_VIEW_TYPE -> {
                (holder.itemView as? LearningPathwayCourseBottomItemView)?.init(item.courseItem!!, onDeleteCourseClicked)
            }
            COURSE_SINGLE_VIEW_TYPE -> {
                (holder.itemView as? LearningPathwayCourseSingleItemView)?.init(item.courseItem!!, onDeleteCourseClicked)
            }
            REQUIRED_COURSE_VIEW_TYPE -> {
                (holder.itemView as? LearningPathwayRequiredCourseItemView)?.init(item.requiredCourseItem!!, onShowRequiredCourseClicked)
            }
            else -> {
                (holder.itemView as? LearningPathwayHeaderItemView)?.init(item.header!!, onAddCourseClicked)
            }
        }
    }

    data class Item(
        val header: LearningPathwayHeaderItem? = null,
        val courseItem: LearningPathwayCourseItem? = null,
        val requiredCourseItem: LearningPathwayRequiredCourseItem? = null
    )

    private class LearningPathwayDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            val isSameSelectedCourseIds = if (oldItem.header?.selectedCourseIds != null && newItem.header?.selectedCourseIds != null) {
                oldItem.header.selectedCourseIds.containsAll(newItem.header.selectedCourseIds) && newItem.header.selectedCourseIds.containsAll(oldItem.header.selectedCourseIds)
            } else {
                !(oldItem.header?.selectedCourseIds != null || newItem.header?.selectedCourseIds != null)
            }

            val isSameRequiredCourses = if (oldItem.requiredCourseItem != null && newItem.requiredCourseItem != null) {
                oldItem.requiredCourseItem.courses.containsAll(newItem.requiredCourseItem.courses) && newItem.requiredCourseItem.courses.containsAll(oldItem.requiredCourseItem.courses)
            } else {
                !(oldItem.requiredCourseItem != null || newItem.requiredCourseItem != null)
            }

            return oldItem.header == newItem.header &&
                    oldItem.courseItem == newItem.courseItem &&
                    oldItem.requiredCourseItem?.term == newItem.requiredCourseItem?.term &&
                    isSameRequiredCourses &&
                    isSameSelectedCourseIds
        }
    }
}