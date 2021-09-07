package whiz.sspark.library.view.screen.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.MenuItemType
import whiz.sspark.library.data.enum.MenuSegmentType
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewMenuStudentFragmentBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.general.custom_divider.CustomWidgetPaddingItemDecoration
import whiz.sspark.library.view.widget.menu.MenuAdapter
import whiz.sspark.library.view.widget.menu.MenuSegmentAdapter
import whiz.sspark.library.view.widget.menu.MenuMemberAdapter

class MenuStudentFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewMenuStudentFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var menuSegmentType = MenuSegmentType.INSTRUCTOR.type

    private var segmentAdapter: MenuSegmentAdapter? = null
    private var memberAdapter: MenuMemberAdapter? = null
    private var menuAdapter: MenuAdapter? = null

    private val members: MutableList<MenuMember> = mutableListOf()

    fun init(student: Student,
             segments: List<MenuSegment>,
             onCameraClicked: () -> Unit,
             onMenuClicked: (String) -> Unit,
             onMemberClicked: (MenuMember) -> Unit,
             onRefresh: () -> Unit) {

        val convertedMember = student.getMenuMember(context)
        with(this.members) {
            clear()
            addAll(convertedMember)
        }

        binding.ivCamera.show(R.drawable.ic_camera)
        binding.vGradientTop.show(R.drawable.bg_primary_gradient_0)
        binding.ivProfile.showUserProfileCircle(student.imageUrl, getGender(student.gender).type)

        binding.tvFirstname.text = student.firstName
        binding.tvLastname.text = student.lastName
        binding.tvIDCardNumber.text = resources.getString(R.string.menu_id_card_number, student.code)
        binding.cvCamera.setOnClickListener {
            onCameraClicked()
        }

        binding.srlMenu.setOnRefreshListener {
            resetMenuSegment()
            onRefresh()
        }

        segmentAdapter = MenuSegmentAdapter(
            context = context,
            segments = segments,
            onSegmentSelected = {
                menuSegmentType = it
                updateMemberAdapter()
            }
        )

        memberAdapter = MenuMemberAdapter(context, onMemberClicked)
        menuAdapter = MenuAdapter(context, onMenuClicked)

        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(false)
        }.build()

        val concatAdapter = ConcatAdapter(config, segmentAdapter, memberAdapter, menuAdapter)

        val layoutManager = GridLayoutManager(context, 12)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (concatAdapter.getItemViewType(position)) {
                    MenuSegmentAdapter.VIEW_TYPE -> 12
                    MenuMemberAdapter.VIEW_TYPE -> 12
                    MenuAdapter.MENU_TYPE -> 12
                    MenuAdapter.NO_MESSAGE_WIDGET_TYPE -> 6
                    MenuAdapter.MESSAGE_WIDGET_TYPE -> 6
                    MenuAdapter.CALENDAR_WIDGET_TYPE -> 6
                    MenuAdapter.GRADE_SUMMARY_WIDGET_TYPE -> 6
                    else -> 12
                }
            }
        }

        with(binding.rvMenu) {
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    CustomDividerMultiItemDecoration(
                        divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                        dividerViewType = listOf(MenuMemberAdapter.VIEW_TYPE, MenuAdapter.MENU_TYPE))
                )
                addItemDecoration(
                    CustomWidgetPaddingItemDecoration(
                        padding = 6.toDP(context),
                        viewTypes = listOf(MenuAdapter.NO_MESSAGE_WIDGET_TYPE, MenuAdapter.MESSAGE_WIDGET_TYPE, MenuAdapter.CALENDAR_WIDGET_TYPE, MenuAdapter.GRADE_SUMMARY_WIDGET_TYPE))
                )
            }

            this.layoutManager = layoutManager
            adapter = concatAdapter
        }

        updateMemberAdapter()
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlMenu.isRefreshing = isLoading == true
    }

    private fun resetMenuSegment() {
        menuSegmentType = MenuSegmentType.INSTRUCTOR.type
        updateMemberAdapter()
        segmentAdapter?.resetSelectedTab()
    }

    private fun updateMemberAdapter() {
        val filterMember = members.filter { it.type.type == menuSegmentType }
        memberAdapter?.submitList(filterMember)
    }

    fun updateStudentProfileImage(profileImageUrl: String, gender: Long) {
        binding.ivProfile.showUserProfileCircle(profileImageUrl, gender)
    }

    fun updateMenu(menusDTO: List<MenuDTO>) {
        val items = mutableListOf<MenuAdapter.Item>()
        menusDTO.forEach { menuDTO ->
            items.add(MenuAdapter.Item(type = menuDTO.type, code = menuDTO.code, title = menuDTO.name))

            menuDTO.items.forEach { menuItemDTO ->
                when (menuItemDTO.type) {
                    MenuItemType.ADVISING_WIDGET.type,
                    MenuItemType.NOTIFICATION_WIDGET.type -> items.add(MenuAdapter.Item(type = menuItemDTO.type, title = menuItemDTO.name, code = menuItemDTO.code, previewMessageItem = PreviewMessageItem()))
                    MenuItemType.CALENDAR_WIDGET.type -> items.add(MenuAdapter.Item(type = menuItemDTO.type, title = menuItemDTO.name,code = menuItemDTO.code, calendarItem = CalendarWidgetItem()))
                    MenuItemType.GRADE_SUMMARY.type -> items.add(MenuAdapter.Item(type = menuItemDTO.type, title = menuItemDTO.name,code = menuItemDTO.code, gradeSummary = listOf()))
                    else -> items.add(MenuAdapter.Item(type = menuItemDTO.type, title = menuItemDTO.name, code = menuItemDTO.code, menuItem = menuItemDTO.convertToAdapterItem()))
                }
            }
        }

        menuAdapter?.submitList(items)
    }

    fun updateAdvisingNote(menuAdvisingNoteDTO: MenuAdvisingNoteDTO) {
        val items = menuAdapter?.currentList ?: listOf()
        val index = items.indexOfFirst { it.type == MenuItemType.ADVISING_WIDGET.type }

        if (index != -1) {
            items.getOrNull(index)?.previewMessageItem = menuAdvisingNoteDTO.convertToPreviewMessageItem()
        }

        menuAdapter?.submitList(items)
    }

    fun updateCalendar(menuCalendarDTO: MenuCalendarDTO) {
        val items = menuAdapter?.currentList ?: listOf()
        val index = items.indexOfFirst { it.type == MenuItemType.CALENDAR_WIDGET.type }

        if (index != -1) {
            items.getOrNull(index)?.calendarItem = menuCalendarDTO.convertToCalendarItem()
        }

        menuAdapter?.submitList(items)
    }

    fun updateNotificationInbox(menuNotificationInboxDTO: MenuNotificationInboxDTO) {
        val items = menuAdapter?.currentList ?: listOf()
        val index = items.indexOfFirst { it.type == MenuItemType.NOTIFICATION_WIDGET.type }

        if (index != -1) {
            items.getOrNull(index)?.previewMessageItem = menuNotificationInboxDTO.convertToPreviewMessageItem()
        }

        menuAdapter?.submitList(items)
    }

    fun updateGradeSummary(grades: List<MenuGradeSummaryDTO>) {
        val items = menuAdapter?.currentList ?: listOf()
        val index = items.indexOfFirst { it.type == MenuItemType.GRADE_SUMMARY.type }

        if (index != -1) {
            items.getOrNull(index)?.gradeSummary = convertToGradeSummaryItem(grades)
        }

        menuAdapter?.submitList(items)
    }
}