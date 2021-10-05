package whiz.sspark.library.view.screen.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.MenuItemType
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewInstructorMenuFragmentBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.general.custom_divider.CustomWidgetPaddingItemDecoration
import whiz.sspark.library.view.widget.menu.MenuAdapter
import whiz.sspark.library.view.widget.menu.MenuMemberAdapter
import whiz.sspark.library.view.widget.menu.MenuSegmentAdapter

class InstructorMenuFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewInstructorMenuFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var menuAdapter: MenuAdapter? = null

    fun init(instructor: Instructor,
             onCameraClicked: () -> Unit,
             onMenuClicked: (String) -> Unit,
             onRefresh: () -> Unit) {

        binding.ivCamera.show(R.drawable.ic_camera)
        binding.vGradientTop.show(R.drawable.bg_primary_gradient_0)
        binding.ivProfile.showUserProfileCircle(instructor.profileImageUrl, getGender(instructor.gender).type)

        binding.tvFirstname.text = convertToFullName(instructor.firstName, null, null, instructor.position)
        binding.tvLastname.text = instructor.lastName
        binding.tvJobPosition.text = instructor.jobPosition
        binding.cvCamera.setOnClickListener {
            onCameraClicked()
        }

        binding.srlMenu.setOnRefreshListener {
            menuAdapter?.resetHeight()
            onRefresh()
        }

        menuAdapter = MenuAdapter(context, onMenuClicked)

        val layoutManager = GridLayoutManager(context, 12)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (menuAdapter!!.getItemViewType(position)) {
                    MenuSegmentAdapter.VIEW_TYPE -> 12
                    MenuMemberAdapter.VIEW_TYPE -> 12
                    MenuAdapter.MENU_TYPE -> 12
                    MenuAdapter.NO_MESSAGE_WIDGET_TYPE -> 6
                    MenuAdapter.MESSAGE_WIDGET_TYPE -> 6
                    MenuAdapter.CALENDAR_WIDGET_TYPE -> 6
                    MenuAdapter.DOWNLOAD_FAILED_WIDGET_TYPE -> 6
                    else -> 12
                }
            }
        }

        with(binding.rvMenu) {
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    CustomDividerMultiItemDecoration(
                        divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                        dividerViewType = listOf(
                            MenuMemberAdapter.VIEW_TYPE,
                            MenuAdapter.MENU_TYPE))
                )
                addItemDecoration(
                    CustomWidgetPaddingItemDecoration(
                        padding = 6.toDP(context),
                        viewTypes = listOf(
                            MenuAdapter.DOWNLOAD_FAILED_WIDGET_TYPE,
                            MenuAdapter.NO_MESSAGE_WIDGET_TYPE,
                            MenuAdapter.MESSAGE_WIDGET_TYPE,
                            MenuAdapter.CALENDAR_WIDGET_TYPE))
                )
            }

            this.layoutManager = layoutManager
            adapter = menuAdapter
        }
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlMenu.isRefreshing = isLoading == true
    }

    fun updateProfileImage(profileImageUrl: String, gender: Long) {
        binding.ivProfile.showUserProfileCircle(profileImageUrl, gender)
    }

    fun updateMenu(items: List<MenuAdapter.Item>) {
        menuAdapter?.submitList(items)
    }

    fun updateCalendar(menuCalendarDTO: MenuCalendarDTO) {
        val items = menuAdapter?.currentList ?: listOf()
        val index = items.indexOfFirst { it.type == MenuItemType.CALENDAR_WIDGET.type }

        if (index != -1) {
            items.getOrNull(index)?.isShowDownloadFailedWidget = false
            items.getOrNull(index)?.calendarItem = menuCalendarDTO.convertToCalendarItem()
            menuAdapter?.notifyItemChanged(index)
        }
    }

    fun updateNotificationInbox(menuNotificationInboxDTO: MenuNotificationInboxDTO) {
        val items = menuAdapter?.currentList ?: listOf()
        val index = items.indexOfFirst { it.type == MenuItemType.NOTIFICATION_WIDGET.type }

        if (index != -1) {
            items.getOrNull(index)?.isShowDownloadFailedWidget = false
            items.getOrNull(index)?.previewMessageItem = menuNotificationInboxDTO.convertToPreviewMessageItem()
            menuAdapter?.notifyItemChanged(index)
        }
    }

    fun updateFailedWidget(menuType: String) {
        val items = menuAdapter?.currentList ?: listOf()
        val index = items.indexOfFirst { it.type == menuType }

        if (index != -1) {
            items.getOrNull(index)?.isShowDownloadFailedWidget = true
            menuAdapter?.notifyItemChanged(index)
        }
    }
}