package whiz.sspark.library.view.widget.collaboration.class_member_with_chat

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.data.entity.ClassMemberItem
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.ItemListTitleView

class ClassMemberWithChatAdapter(private val context: Context,
                                 private val items: List<ClassMemberItem>,
                                 private val onChatMemberClicked: (ClassMember) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class HomeroomMemberAdapterViewType(val type: Int) {
        TITLE(0),
        INSTRUCTOR_MEMBER(1),
        STUDENT_MEMBER(2),
    }

    private class ItemListTitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private class HomeroomMemberStudentViewHolder(val view: View): RecyclerView.ViewHolder(view)

    private class HomeroomMemberInstructorViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HomeroomMemberAdapterViewType.TITLE.type -> ItemListTitleViewHolder(
                ItemListTitleView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                }
            )
            HomeroomMemberAdapterViewType.INSTRUCTOR_MEMBER.type -> HomeroomMemberInstructorViewHolder(
                ClassMemberWithChatInstructorView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                }
            )
            else -> HomeroomMemberStudentViewHolder(ClassMemberWithChatStudentView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        val isNextItemTitle = getItemViewType(position + 1) == HomeroomMemberAdapterViewType.TITLE.type
        val isPreviousItemTitle = getItemViewType(position - 1) == HomeroomMemberAdapterViewType.TITLE.type

        item?.let {
            when {
                item.instructor != null -> {
                    (holder.itemView as? ClassMemberWithChatInstructorView)?.apply {
                        init(
                            member = item.instructor,
                            isChatEnable = item.isChatEnable,
                            onChatMemberClicked = onChatMemberClicked
                        )

                        setDarkModeBackground(isNextItemTitle, isPreviousItemTitle)
                    }
                }
                item.student != null -> {
                    (holder.itemView as? ClassMemberWithChatStudentView)?.apply {
                        init(
                            member = item.student,
                            isChatEnable = item.isChatEnable,
                            onChatMemberClicked = onChatMemberClicked
                        )

                        setDarkModeBackground(isNextItemTitle, isPreviousItemTitle)
                    }
                }
                else -> (holder.itemView as? ItemListTitleView)?.init(item.title ?: "")
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        val item = items.getOrNull(position)
        return when {
            item?.student != null -> HomeroomMemberAdapterViewType.STUDENT_MEMBER.type
            item?.instructor != null -> HomeroomMemberAdapterViewType.INSTRUCTOR_MEMBER.type
            else -> HomeroomMemberAdapterViewType.TITLE.type
        }
    }
}