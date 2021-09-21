package whiz.sspark.library.view.screen.notification_inbox

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.databinding.ViewNotificationInboxActivityBinding
import whiz.sspark.library.extension.showViewStateX
import whiz.sspark.library.utility.PaginationScrollListener
import whiz.sspark.library.view.widget.notification_inbox.NotificationInboxAdapter

class NotificationInboxActivity : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewNotificationInboxActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var isLastPage = false

    fun init(onRefresh: () -> Unit,
             onLoadMore: () -> Unit) {
        val layoutManager =  LinearLayoutManager(context)
        with(binding.rvInbox) {
            this.layoutManager = layoutManager
            adapter = NotificationInboxAdapter()
            addOnScrollListener(paginationScrollListener(layoutManager, onLoadMore))
        }

        binding.srlContainer.setOnRefreshListener(onRefresh)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun setIsLastPage(isLastPage: Boolean) {
        this.isLastPage = isLastPage
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun updateItem(items: List<NotificationInboxAdapter.Item>) {
        (binding.rvInbox.adapter as? NotificationInboxAdapter)?.submitList(items)
    }

    private fun paginationScrollListener(layoutManager: LinearLayoutManager, onLoadMore: () -> Unit): PaginationScrollListener {
        return object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return binding.srlContainer.isRefreshing
            }

            override fun loadMoreItems() {
                onLoadMore()
            }
        }
    }
}