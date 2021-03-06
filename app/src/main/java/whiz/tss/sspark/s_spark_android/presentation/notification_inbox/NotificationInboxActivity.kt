package whiz.tss.sspark.s_spark_android.presentation.notification_inbox

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.Inbox
import whiz.sspark.library.data.entity.InboxItemDTO
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.data.static.PagingConfiguration
import whiz.sspark.library.data.viewModel.NotificationInboxViewModel
import whiz.sspark.library.extension.*
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.notification_inbox.NotificationInboxAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityNotificationInboxBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class NotificationInboxActivity : BaseActivity() {

    private val viewModel: NotificationInboxViewModel by viewModel()

    private lateinit var binding: ActivityNotificationInboxBinding

    private var dataWrapper: DataWrapperX<Any>? = null
    private var inboxes: MutableList<InboxItemDTO> = mutableListOf()

    private var currentPage = PagingConfiguration.INITIAL_PAGE
    private var totalPage = PagingConfiguration.INITIAL_TOTAL_PAGE
    private var isLoadMoreData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationInboxBinding.inflate(layoutInflater)
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
            initView()

            if (dataWrapper != null) {
                binding.vInbox.setLatestUpdatedText(dataWrapper)
                updateAdapterItem()
            } else {
                viewModel.getLatestNotification(PagingConfiguration.INITIAL_PAGE, PagingConfiguration.PAGE_SIZE)
            }
        } else {
            initView()
            viewModel.getLatestNotification(PagingConfiguration.INITIAL_PAGE, PagingConfiguration.PAGE_SIZE)
        }
    }

    override fun initView() {
        binding.vProfile.init(lifecycle)
        binding.vInbox.init(
            onRefresh = {
                viewModel.getLatestNotification(PagingConfiguration.INITIAL_PAGE, PagingConfiguration.PAGE_SIZE)
            },
            onLoadMore = {
                if (!isLoadMoreData) {
                    isLoadMoreData = true
                    val nextPage = currentPage + PagingConfiguration.PAGE_INCREASE_STEP
                    viewModel.getNotifications(nextPage, PagingConfiguration.PAGE_SIZE)
                }
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vInbox.setSwipeRefreshLayout(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            dataWrapper = it
            binding.vInbox.setLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.notificationResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                currentPage += PagingConfiguration.PAGE_INCREASE_STEP
                inboxes.addAll(it.item)
                checkIsLastPage()
                updateAdapterItem()
                isLoadMoreData = false
            }
        }

        viewModel.latestNotificationResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                currentPage = PagingConfiguration.INITIAL_PAGE
                totalPage = it.totalPage

                with(inboxes) {
                    clear()
                    addAll(it.item)
                }

                checkIsLastPage()
                updateAdapterItem()
            }
        }
    }

    override fun observeError() {
        viewModel.notificationErrorResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                showApiResponseXAlert(this, it)
                isLoadMoreData = false
            }
        }

        viewModel.notificationErrorResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                showApiResponseXAlert(this, it)
                isLoadMoreData = false
            }
        }

        viewModel.errorMessage.observe(this ) {
            it.getContentIfNotHandled()?.let {
                showAlertWithOkButton(it)
            }
        }
    }

    private fun updateAdapterItem() {
        val items = mutableListOf<NotificationInboxAdapter.Item>()
        val sortedInbox = inboxes.sortedByDescending { it.datetime.toLocalDate()!! }
        val groupedInbox = sortedInbox.groupBy { it.datetime.toLocalDate()!!.convertToDateString(DateTimePattern.dayAbbreviateMonthFormatEn) }

        groupedInbox.forEach {
            val date = it.value.firstOrNull()?.datetime
            items.add(NotificationInboxAdapter.Item(date = date))

            it.value.forEach {
                val inbox = Inbox(
                    isRead = it.isRead,
                    title = it.title,
                    detail = it.message,
                    date = it.datetime
                )

                items.add(NotificationInboxAdapter.Item(inbox = inbox))
            }
        }

        binding.vInbox.updateItem(items)
    }

    private fun checkIsLastPage() {
        val isLastPage = currentPage >= totalPage
        binding.vInbox.setIsLastPage(isLastPage)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        dataWrapper = savedInstanceState.getString("dataWrapper")?.toObject()
        inboxes = savedInstanceState.getString("inboxes")?.toObjects(Array<InboxItemDTO>::class.java) ?: mutableListOf()
        currentPage = savedInstanceState.getInt("currentPage")
        totalPage = savedInstanceState.getInt("totalPage")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("dataWrapper", dataWrapper?.toJson())
        outState.putString("inboxes", inboxes.toJson())
        outState.putInt("currentPage", currentPage)
        outState.putInt("totalPage", totalPage)
    }
}