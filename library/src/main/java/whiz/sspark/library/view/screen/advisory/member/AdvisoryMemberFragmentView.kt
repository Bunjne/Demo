package whiz.sspark.library.view.screen.advisory.member

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.databinding.ViewAdvisoryMemberFragmentBinding
import whiz.sspark.library.utility.updateItem
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.widget.advisory.member.AdvisoryMemberAdapter

class AdvisoryMemberFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAdvisoryMemberFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(items: List<AdvisoryMemberAdapter.Item>,
             onRefresh: () -> Unit,
             onChatMemberClicked: (ClassMember) -> Unit) {

        binding.srlMember.setOnRefreshListener {
            onRefresh()
        }

        with(binding.rvMember) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                CustomDividerMultiItemDecoration(
                    divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                    dividerViewType = listOf(AdvisoryMemberAdapter.AdvisoryMemberAdapterViewType.STUDENT_MEMBER.type, AdvisoryMemberAdapter.AdvisoryMemberAdapterViewType.INSTRUCTOR_MEMBER.type)
                )
            )

            adapter = AdvisoryMemberAdapter(
                context = context,
                items = items,
                onChatMemberClicked = onChatMemberClicked
            )
        }
    }

    fun updateRecyclerViewItems(currentItems: MutableList<AdvisoryMemberAdapter.Item>, newItems: List<AdvisoryMemberAdapter.Item>) {
        binding.rvMember.adapter?.updateItem(currentItems, newItems)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlMember.isRefreshing = isLoading
    }
}