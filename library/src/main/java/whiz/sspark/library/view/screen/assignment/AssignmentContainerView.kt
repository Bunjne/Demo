package whiz.sspark.library.view.screen.assignment

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.databinding.ViewAssignmentContainerBinding
import whiz.sspark.library.utility.PaginationScrollListener
import whiz.sspark.library.view.widget.assignment.AssignmentAdapter

class AssignmentContainerView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAssignmentContainerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var isLastPage = false
    private var isLoading = false

    fun init(progressbarColor: Int,
             onAssignmentClicked: (Assignment) -> Unit,
             onRefresh: () -> Unit,
             onLoadMore: () -> Unit) {
        val layoutManager =  LinearLayoutManager(context)

        with(binding.rvAssignment) {
            this.layoutManager = layoutManager
            adapter = AssignmentAdapter(
                progressbarColor = progressbarColor,
                onAssignmentClicked = onAssignmentClicked
            )

            addOnScrollListener(paginationScrollListener(layoutManager, onLoadMore))
        }

        binding.srlContainer.setOnRefreshListener(onRefresh)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading = isLoading

        val items = (binding.rvAssignment.adapter as? AssignmentAdapter)?.currentList ?: listOf()
        val index = items.lastIndexOf(AssignmentAdapter.AssignmentItem.Loading(!this.isLoading))
        if (index != -1) {
            (items[index] as AssignmentAdapter.AssignmentItem.Loading).isShowing = this.isLoading
            binding.rvAssignment.adapter?.notifyItemChanged(index)
        }
    }

    fun setIsLastPage(isLastPage: Boolean) {
        this.isLastPage = isLastPage
    }

    fun updateItem(items: List<AssignmentAdapter.AssignmentItem>) {
        (binding.rvAssignment.adapter as? AssignmentAdapter)?.submitList(items)

        if (items.isEmpty()) {
            binding.tvNoAssignment.visibility = View.VISIBLE
            binding.rvAssignment.visibility = View.GONE
        } else {
            binding.tvNoAssignment.visibility = View.GONE
            binding.rvAssignment.visibility = View.VISIBLE
        }
    }

    private fun paginationScrollListener(layoutManager: LinearLayoutManager, onLoadMore: () -> Unit): PaginationScrollListener {
        return object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return binding.srlContainer.isRefreshing || isLoading
            }

            override fun loadMoreItems() {
                onLoadMore()
            }
        }
    }
}