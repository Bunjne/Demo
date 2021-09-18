package whiz.sspark.library.view.screen.collaboration.class_member.student

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewStudentClassMemberFragmentBinding
import whiz.sspark.library.utility.updateItem
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.widget.collaboration.class_member.ClassMemberAdapter

class StudentClassMemberFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewStudentClassMemberFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(items: List<ClassMemberAdapter.Item>,
             onRefresh: () -> Unit) {

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

            adapter = ClassMemberAdapter(
                context = context,
                items = items
            )
        }
    }

    fun updateRecyclerViewItems(currentItems: MutableList<ClassMemberAdapter.Item>, newItems: List<ClassMemberAdapter.Item>) {
        binding.rvMember.adapter?.updateItem(currentItems, newItems)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlMember.isRefreshing = isLoading
    }
}