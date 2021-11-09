package whiz.sspark.library.view.screen.assignment

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.databinding.ViewAssignmentActivityBinding
import whiz.sspark.library.extension.showViewStateX
import whiz.sspark.library.view.widget.assignment.AssignmentAdapter

class AssignmentActivityView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAssignmentActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(progressbarColor: Int,
             onAssignmentClicked: (Assignment) -> Unit,
             onRefresh: () -> Unit,
             onLoadMore: () -> Unit) {
        binding.vAssignment.init(
            progressbarColor = progressbarColor,
            onAssignmentClicked = onAssignmentClicked,
            onRefresh = onRefresh,
            onLoadMore = onLoadMore
        )
    }

    fun setIsLastPage(isLastPage: Boolean) {
        binding.vAssignment.setIsLastPage(isLastPage)
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.vAssignment.setSwipeRefreshLayout(isLoading)
    }

    fun setIsLoading(isLoading: Boolean) {
        binding.vAssignment.setIsLoading(isLoading)
    }

    fun updateItem(items: List<AssignmentAdapter.AssignmentItem>) {
        binding.vAssignment.updateItem(items)
    }

    fun clearOldItem(onClearSuccess: () -> Unit) {
        binding.vAssignment.clearOldItem(onClearSuccess)
    }
}