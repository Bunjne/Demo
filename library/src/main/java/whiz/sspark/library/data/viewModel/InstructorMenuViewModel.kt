package whiz.sspark.library.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.MenuItemType
import whiz.sspark.library.data.repository.InstructorMenuRepositoryImpl
import whiz.sspark.library.data.repository.StudentMenuRepositoryImpl

class InstructorMenuViewModel(private val instructorMenuRepositoryImpl: InstructorMenuRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _menuResponse = MutableLiveData<List<MenuDTO>>()
    val menuResponse: LiveData<List<MenuDTO>>
        get() = _menuResponse

    private val _menuErrorResponse = MutableLiveData<ApiResponseX?>()
    val menuErrorResponse: LiveData<ApiResponseX?>
        get() = _menuErrorResponse

    private val _notificationInboxResponse = MutableLiveData<MenuNotificationInboxDTO>()
    val notificationInboxResponse: LiveData<MenuNotificationInboxDTO>
        get() = _notificationInboxResponse

    private val _notificationInboxErrorResponse = MutableLiveData<ApiResponseX?>()
    val notificationInboxErrorResponse: LiveData<ApiResponseX?>
        get() = _notificationInboxErrorResponse

    private val _calendarResponse = MutableLiveData<MenuCalendarDTO>()
    val calendarResponse: LiveData<MenuCalendarDTO>
        get() = _calendarResponse

    private val _calendarErrorResponse = MutableLiveData<ApiResponseX?>()
    val calendarErrorResponse: LiveData<ApiResponseX?>
        get() = _calendarErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getMenu() {
        viewModelScope.launch {
            instructorMenuRepositoryImpl.getMenu()
                .onStart {
                    _viewLoading.value = true
                }
                .onCompletion {
                    _viewLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _menuResponse.value = it
                    } ?: run {
                        _menuErrorResponse.value = it.error
                    }
                }
        }
    }

    private fun getNotificationInbox() {
        viewModelScope.launch {
            instructorMenuRepositoryImpl.getNotificationInbox()
                .onStart {
                    _viewLoading.value = true
                }
                .onCompletion {
                    _viewLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _notificationInboxResponse.value = it
                    } ?: run {
                        _notificationInboxErrorResponse.value = it.error
                    }
                }
        }
    }

    private fun getCalendar() {
        viewModelScope.launch {
            instructorMenuRepositoryImpl.getCalendar()
                .onStart {
                    _viewLoading.value = true
                }
                .onCompletion {
                    _viewLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _calendarResponse.value = it
                    } ?: run {
                        _calendarErrorResponse.value = it.error
                    }
                }
        }
    }

    fun fetchWidget(menusDTO: List<MenuDTO>) {
        menusDTO.forEach {
            it.items.forEach {
                when (it.type) {
                    MenuItemType.NOTIFICATION_WIDGET.type -> getNotificationInbox()
                    MenuItemType.CALENDAR_WIDGET.type -> getCalendar()
                }
            }
        }
    }
}