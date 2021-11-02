package whiz.sspark.library.view.widget.learning_pathway

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.Course
import whiz.sspark.library.data.entity.LearningPathwayCourseItem
import whiz.sspark.library.data.entity.LearningPathwayHeaderItem
import whiz.sspark.library.data.entity.Term

class LearningPathwayAdapter(private val isPlanEditable: Boolean,
                             private val onAddCourseClicked: (Term, Int, Int, Int, List<String>) -> Unit,
                             private val onDeleteCourseClicked: (Int, Int, String) -> Unit,
                             private val onShowRequiredCourseClicked: (Term, List<Course>) -> Unit): ListAdapter<LearningPathwayAdapter.Item, RecyclerView.ViewHolder>(LearningPathwayDiffCallback()) {

    companion object {
        const val HEADER_VIEW_TYPE = 1111
        const val COURSE_VIEW_MIDDLE_TYPE = 2222
        const val COURSE_VIEW_BOTTOM_TYPE = 3333
        const val COURSE_COUNT_VIEW_TYPE = 4444
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

        return when {
            item?.courseItem != null -> {
                val isNextCourseExisted = nextItem?.courseItem != null

                if (isNextCourseExisted) {
                    COURSE_VIEW_MIDDLE_TYPE
                } else {
                    COURSE_VIEW_BOTTOM_TYPE
                }
            }
            item?.courseCount != null -> {
                COURSE_COUNT_VIEW_TYPE
            }
            else -> {
                HEADER_VIEW_TYPE
            }
        }
    }

    class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseMiddleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseBottomViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CourseCountViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            COURSE_COUNT_VIEW_TYPE -> {
                CourseCountViewHolder(LearningPathwayCourseCountItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            COURSE_VIEW_MIDDLE_TYPE -> {
                CourseMiddleViewHolder(LearningPathwayCourseMiddleItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            }
            COURSE_VIEW_BOTTOM_TYPE -> {
                CourseBottomViewHolder(LearningPathwayCourseBottomItemView(parent.context).apply {
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

        val nextItemViewType = getItemViewType(position + 1)

        when(viewType) {
            COURSE_COUNT_VIEW_TYPE -> (holder.itemView as? LearningPathwayCourseCountItemView)?.init(item.courseCount!!)
            COURSE_VIEW_MIDDLE_TYPE -> (holder.itemView as? LearningPathwayCourseMiddleItemView)?.init(item.courseItem!!, isPlanEditable, onDeleteCourseClicked)
            COURSE_VIEW_BOTTOM_TYPE -> (holder.itemView as? LearningPathwayCourseBottomItemView)?.init(item.courseItem!!, isPlanEditable, onDeleteCourseClicked)
            else -> {
                (holder.itemView as? LearningPathwayHeaderItemView)?.apply {
                    init(item.header!!, isPlanEditable, onAddCourseClicked, onShowRequiredCourseClicked)

                    val isShowBottomCornerRadius = nextItemViewType == HEADER_VIEW_TYPE
                    showBottomCornerRadius(isShowBottomCornerRadius)
                }
            }
        }
    }

    data class Item(
        val header: LearningPathwayHeaderItem? = null,
        val courseItem: LearningPathwayCourseItem? = null,
        val courseCount: Int? = null
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

            val isSameRequiredCourses = if (oldItem.header?.basicCourses != null && newItem.header?.basicCourses != null) {
                oldItem.header.basicCourses.containsAll(newItem.header.basicCourses) && newItem.header.basicCourses.containsAll(oldItem.header.basicCourses)
            } else {
                !(oldItem.header?.basicCourses != null || newItem.header?.basicCourses != null)
            }

            return oldItem.header == newItem.header &&
                    oldItem.courseCount == newItem.courseCount &&
                    oldItem.courseItem == newItem.courseItem &&
                    isSameRequiredCourses &&
                    isSameSelectedCourseIds
        }
    }
}