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
            item?.requiredCourse != null -> {
                REQUIRED_COURSE_VIEW_TYPE
            }
            item?.course != null && previousItem?.course == null && nextItem?.course != null -> {
                COURSE_TOP_VIEW_TYPE
            }
            item?.course != null && previousItem?.course != null && nextItem?.course != null -> {
                COURSE_MIDDLE_VIEW_TYPE
            }
            item?.course != null && previousItem?.course != null && nextItem?.course == null -> {
                COURSE_BOTTOM_VIEW_TYPE
            }
            item?.course != null && previousItem?.course == null && nextItem?.course == null -> {
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
                (holder.itemView as? LearningPathwayCourseTopItemView)?.init(item.course!!, onDeleteCourseClicked)
            }
            COURSE_MIDDLE_VIEW_TYPE -> {
                (holder.itemView as? LearningPathwayCourseMiddleItemView)?.init(item.course!!, onDeleteCourseClicked)
            }
            COURSE_BOTTOM_VIEW_TYPE -> {
                (holder.itemView as? LearningPathwayCourseBottomItemView)?.init(item.course!!, onDeleteCourseClicked)
            }
            COURSE_SINGLE_VIEW_TYPE -> {
                (holder.itemView as? LearningPathwayCourseSingleItemView)?.init(item.course!!, onDeleteCourseClicked)
            }
            REQUIRED_COURSE_VIEW_TYPE -> {
                (holder.itemView as? LearningPathwayRequiredCourseItemView)?.init(item.requiredCourse!!, onShowRequiredCourseClicked)
            }
            else -> {
                (holder.itemView as? LearningPathwayHeaderItemView)?.init(item.header!!, onAddCourseClicked)
            }
        }
    }

    data class Item(
        val header: LearningPathwayHeaderItem? = null,
        val course: LearningPathwayCourse? = null,
        val requiredCourse: LearningPathwayRequiredCourse? = null
    )
}

private class LearningPathwayDiffCallback : DiffUtil.ItemCallback<LearningPathwayAdapter.Item>() {
    override fun areItemsTheSame(oldItem: LearningPathwayAdapter.Item, newItem: LearningPathwayAdapter.Item): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: LearningPathwayAdapter.Item, newItem: LearningPathwayAdapter.Item): Boolean {
        val sameSelectedCourseIds = if (oldItem.header?.selectedCourseIds != null && newItem.header?.selectedCourseIds != null) {
            oldItem.header.selectedCourseIds.containsAll(newItem.header.selectedCourseIds) && newItem.header.selectedCourseIds.containsAll(oldItem.header.selectedCourseIds)
        } else {
            !(oldItem.header?.selectedCourseIds != null || newItem.header?.selectedCourseIds != null)
        }

        val sameRequiredCourses = if (oldItem.requiredCourse != null && newItem.requiredCourse != null) {
            oldItem.requiredCourse.courses.containsAll(newItem.requiredCourse.courses) && newItem.requiredCourse.courses.containsAll(oldItem.requiredCourse.courses)
        } else {
            !(oldItem.requiredCourse != null || newItem.requiredCourse != null)
        }

        return oldItem.header == newItem.header &&
                oldItem.course == newItem.course &&
                oldItem.requiredCourse?.term == newItem.requiredCourse?.term &&
                sameRequiredCourses &&
                sameSelectedCourseIds
    }
}