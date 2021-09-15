package whiz.sspark.library.view.widget.collaboration.class_member

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.view.widget.base.ItemListTitleView

class ClassMemberAdapter(private val context: Context,
                         private val userId: String,
                         private val items: List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ClassMemberAdapterViewType(val type: Int) {
        TITLE(0),
        INSTRUCTOR_MEMBER(1),
        STUDENT_MEMBER(2),
    }

    private var instructorCount = 0

    fun setInstructorCount(instructorCount: Int) {
        this.instructorCount = instructorCount
    }

    class ItemListTitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class ClassMemberStudentViewHolder(val view: View): RecyclerView.ViewHolder(view)

    class ClassMemberInstructorViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ClassMemberAdapterViewType.TITLE.type -> ItemListTitleViewHolder(
                ItemListTitleView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            ClassMemberAdapterViewType.INSTRUCTOR_MEMBER.type -> ClassMemberInstructorViewHolder(
                ClassMemberInstructorView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            else -> ClassMemberStudentViewHolder(ClassMemberStudentView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        val isNextItemTitle = getItemViewType(position + 1) == ClassMemberAdapterViewType.TITLE.type
        val isPreviousItemTitle = getItemViewType(position - 1) == ClassMemberAdapterViewType.TITLE.type

        item?.let {
            when {
                item.instructor != null -> {
                    (holder.itemView as? ClassMemberInstructorView)?.apply {
                        init(item)

                        if (isPreviousItemTitle) {
                            setPadding(0, (-2).toDP(context), 0, 0)
                        } else {
                            setPadding(0, 0, 0, 0)
                        }

                        setDarkModeBackground(isNextItemTitle, isPreviousItemTitle)
                    }
                }
                item.student != null -> {
                    (holder.itemView as? ClassMemberStudentView)?.apply {
                        val isSelf = item.student.userId == userId

                        val studentIndex = (position - instructorCount) - 1

                        init(item, studentIndex, isSelf)

                        when {
                            position == items.lastIndex -> setPadding(0, 0, 0, 16.toDP(context))
                            isPreviousItemTitle -> setPadding(0, (-2).toDP(context), 0, 0)
                            else -> setPadding(0, 0, 0, 0)
                        }

                        setDarkModeBackground(isNextItemTitle, isPreviousItemTitle)
                    }
                }
                else -> {
                    (holder.itemView as? ItemListTitleView)?.apply {
                        init(item.title ?: "")

                        setPadding(0, 8.toDP(context), 0, (-2).toDP(context))
                    }
                }
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        val item = items.getOrNull(position)
        return when {
            item?.student != null -> ClassMemberAdapterViewType.STUDENT_MEMBER.type
            item?.instructor != null -> ClassMemberAdapterViewType.INSTRUCTOR_MEMBER.type
            else -> ClassMemberAdapterViewType.TITLE.type
        }
    }

    data class Item(
        val title: String? = null,
        val instructor: ClassMember? = null,
        val student: ClassMember? = null,
    )
}