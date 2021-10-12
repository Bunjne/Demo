package whiz.sspark.library.view.widget.homeroom.member

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.ItemListTitleView

class HomeroomMemberAdapter(private val context: Context,
                            private val items: List<Item>,
                            private val onChatMemberClicked: (ClassMember) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class HomeroomMemberAdapterViewType(val type: Int) {
        TITLE(0),
        INSTRUCTOR_MEMBER(1),
        STUDENT_MEMBER(2),
    }

    class ItemListTitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class HomeroomMemberStudentViewHolder(val view: View): RecyclerView.ViewHolder(view)

    class HomeroomMemberInstructorViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HomeroomMemberAdapterViewType.TITLE.type -> ItemListTitleViewHolder(
                ItemListTitleView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            HomeroomMemberAdapterViewType.INSTRUCTOR_MEMBER.type -> HomeroomMemberInstructorViewHolder(
                HomeroomMemberInstructorView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                })
            else -> HomeroomMemberStudentViewHolder(HomeroomMemberStudentView(context).apply {
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
                    (holder.itemView as? HomeroomMemberInstructorView)?.apply {
                        init(
                            member = item.instructor,
                            isChatEnable = item.isChatEnable,
                            onChatMemberClicked = onChatMemberClicked
                        )

                        setDarkModeBackground(isNextItemTitle, isPreviousItemTitle)
                    }
                }
                item.student != null -> {
                    (holder.itemView as? HomeroomMemberStudentView)?.apply {
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
            item?.student != null -> HomeroomMemberAdapterViewType.STUDENT_MEMBER.type
            item?.instructor != null -> HomeroomMemberAdapterViewType.INSTRUCTOR_MEMBER.type
            else -> HomeroomMemberAdapterViewType.TITLE.type
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