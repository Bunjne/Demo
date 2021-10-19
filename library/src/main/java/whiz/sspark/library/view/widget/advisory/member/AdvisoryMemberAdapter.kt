package whiz.sspark.library.view.widget.advisory.member

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.ItemListTitleView

class AdvisoryMemberAdapter(private val context: Context,
                            private val items: List<Item>,
                            private val onChatMemberClicked: (ClassMember) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class AdvisoryMemberAdapterViewType(val type: Int) {
        TITLE(0),
        INSTRUCTOR_MEMBER(1),
        STUDENT_MEMBER(2),
    }

    class ItemListTitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class AdvisoryMemberStudentViewHolder(val view: View): RecyclerView.ViewHolder(view)

    class AdvisoryMemberInstructorViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AdvisoryMemberAdapterViewType.TITLE.type -> ItemListTitleViewHolder(
                ItemListTitleView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            AdvisoryMemberAdapterViewType.INSTRUCTOR_MEMBER.type -> AdvisoryMemberInstructorViewHolder(
                AdvisoryMemberInstructorView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            else -> AdvisoryMemberStudentViewHolder(AdvisoryMemberStudentView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        val isNextItemTitle = getItemViewType(position + 1) == AdvisoryMemberAdapterViewType.TITLE.type
        val isPreviousItemTitle = getItemViewType(position - 1) == AdvisoryMemberAdapterViewType.TITLE.type

        item?.let {
            when {
                item.instructor != null -> {
                    (holder.itemView as? AdvisoryMemberInstructorView)?.apply {
                        init(
                            member = item.instructor,
                            isChatEnable = item.isChatEnable,
                            onChatMemberClicked = onChatMemberClicked
                        )

                        setDarkModeBackground(isNextItemTitle, isPreviousItemTitle)
                    }
                }
                item.student != null -> {
                    (holder.itemView as? AdvisoryMemberStudentView)?.apply {
                        init(
                            member = item.student,
                            isSelf = item.isSelf,
                            isChatEnable = item.isChatEnable,
                            onChatMemberClicked = onChatMemberClicked
                        )

                        setDarkModeBackground(isNextItemTitle, isPreviousItemTitle)
                    }
                }
                else -> {
                    (holder.itemView as? ItemListTitleView)?.init(item.title ?: "")
                }
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        val item = items.getOrNull(position)
        return when {
            item?.student != null -> AdvisoryMemberAdapterViewType.STUDENT_MEMBER.type
            item?.instructor != null -> AdvisoryMemberAdapterViewType.INSTRUCTOR_MEMBER.type
            else -> AdvisoryMemberAdapterViewType.TITLE.type
        }
    }

    data class Item(
        val title: String? = null,
        val instructor: ClassMember? = null,
        val student: ClassMember? = null,
        val isSelf: Boolean = false,
        val isChatEnable: Boolean = false
    )
}