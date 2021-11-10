package whiz.sspark.library.view.screen.collaboration.class_member.student

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.data.entity.ClassMemberItem
import whiz.sspark.library.databinding.ViewStudentClassMemberFragmentBinding
import whiz.sspark.library.utility.updateItem
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.widget.collaboration.class_member.ClassMemberAdapter
import whiz.sspark.library.view.widget.collaboration.class_member_with_chat.ClassMemberWithChatAdapter

class StudentClassMemberFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewStudentClassMemberFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onRefresh: () -> Unit) {

        binding.srlMember.setOnRefreshListener {
            onRefresh()
        }

        with(binding.rvMember) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                CustomDividerMultiItemDecoration(
                    divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                    dividerViewType = listOf(ClassMemberAdapter.ClassMemberAdapterViewType.STUDENT_MEMBER.type, ClassMemberAdapter.ClassMemberAdapterViewType.INSTRUCTOR_MEMBER.type)
                )
            )
        }
    }

    fun setClassMemberAdapter(items: MutableList<ClassMemberItem>) {
        binding.rvMember.adapter = ClassMemberAdapter(
            context = context,
            items = items
        )
    }

    fun setClassMemberWithChatAdapter(items: MutableList<ClassMemberItem>, onChatMemberClicked: (ClassMember) -> Unit) {
        binding.rvMember.adapter = ClassMemberWithChatAdapter(
            context = context,
            items = items,
            onChatMemberClicked = onChatMemberClicked
        )
    }

    fun updateMemberRecyclerViewItems(currentItems: MutableList<ClassMemberItem>, newItems: List<ClassMemberItem>) {
        binding.rvMember.adapter?.updateItem(currentItems, newItems)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlMember.isRefreshing = isLoading
    }
}