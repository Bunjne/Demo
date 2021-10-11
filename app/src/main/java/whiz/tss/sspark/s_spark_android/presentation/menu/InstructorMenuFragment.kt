package whiz.tss.sspark.s_spark_android.presentation.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.CalendarWidgetItem
import whiz.sspark.library.data.entity.Instructor
import whiz.sspark.library.data.entity.MenuDTO
import whiz.sspark.library.data.entity.PreviewMessageItem
import whiz.sspark.library.data.enum.MenuCode
import whiz.sspark.library.data.enum.MenuItemType
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.data.viewModel.InstructorMenuViewModel
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.menu.MenuAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentInstructorMenuBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.presentation.advisee_list.AdviseeListActivity
import whiz.tss.sspark.s_spark_android.presentation.calendar.CalendarActivity
import whiz.tss.sspark.s_spark_android.utility.logout

class InstructorMenuFragment : BaseFragment() {

    companion object {
        fun newInstance() = InstructorMenuFragment()
    }

    private val viewModel: InstructorMenuViewModel by viewModel()

    private var _binding: FragmentInstructorMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var instructor: Instructor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInstructorMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            profileManager.instructor.collect {
                it?.let {
                    instructor = it

                    initView()

                    viewModel.getMenu()
                } ?: run {
                    logout(requireContext())
                }
            }
        }
    }

    override fun initView() {
        binding.vMenu.init(
            instructor = instructor,
            onCameraClicked = {
                //TODO wait implement camera
            },
            onMenuClicked = { code, title ->
                when(code) {
                    MenuCode.ADVISEE.code -> {
                        val intent = Intent(requireContext(), AdviseeListActivity::class.java).apply {
                            putExtra("title", title)
                        }
                        startActivity(intent)
                    }
                    MenuCode.CALENDAR.code -> {
                        val intent = Intent(requireContext(), CalendarActivity::class.java)
                        startActivity(intent)
                    }
                    MenuCode.LOGOUT.code -> logout(requireContext())
                }
            },
            onRefresh = {
                viewModel.getMenu()
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vMenu.setSwipeRefreshLoading(isLoading)
        }
    }

    override fun observeData() {
        profileManager.instructor.asLiveData().observe(this) {
            it?.let {
                instructor = it
                binding.vMenu.updateProfileImage(it.imageUrl, getGender(it.gender).type)
            }
        }

        viewModel.menuResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                updateAdapterItem(it)
                viewModel.fetchWidget(it)
            }
        }

        viewModel.calendarResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                binding.vMenu.updateCalendar(it)
            }
        }

        viewModel.notificationInboxResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                binding.vMenu.updateNotificationInbox(it)
            }
        }
    }

    override fun observeError() {
        viewModel.menuErrorResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                showApiResponseXAlert(requireContext(), it)
            }
        }

        viewModel.calendarErrorResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                binding.vMenu.updateFailedWidget(MenuItemType.CALENDAR_WIDGET.type)
            }
        }

        viewModel.notificationInboxErrorResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                binding.vMenu.updateFailedWidget(MenuItemType.NOTIFICATION_WIDGET.type)
            }
        }
        
        viewModel.errorMessage.observe(this) {
            it.getContentIfNotHandled()?.let {
                requireContext().showAlertWithOkButton(it)
            }
        }
    }

    private fun updateAdapterItem(menusDTO: List<MenuDTO>) {
        val items = mutableListOf<MenuAdapter.Item>()

        menusDTO.forEach { menuDTO ->
            items.add(MenuAdapter.Item(type = menuDTO.type, code = menuDTO.code, title = menuDTO.name))

            menuDTO.items.forEach { menuItemDTO ->
                when (menuItemDTO.type) {
                    MenuItemType.NOTIFICATION_WIDGET.type -> items.add(MenuAdapter.Item(type = menuItemDTO.type, title = resources.getString(R.string.menu_widget_notification_inbox_text), code = menuItemDTO.code, previewMessageItem = PreviewMessageItem()))
                    MenuItemType.CALENDAR_WIDGET.type -> items.add(MenuAdapter.Item(type = menuItemDTO.type, title = menuItemDTO.name,code = menuItemDTO.code, calendarItem = CalendarWidgetItem()))
                    else -> items.add(MenuAdapter.Item(type = menuItemDTO.type, title = menuItemDTO.name, code = menuItemDTO.code, menuItem = menuItemDTO.convertToAdapterItem()))
                }
            }
        }

        binding.vMenu.updateMenu(items)
    }
}