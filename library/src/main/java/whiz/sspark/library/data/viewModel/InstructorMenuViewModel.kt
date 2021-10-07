package whiz.sspark.library.data.viewModel

import androidx.lifecycle.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.MenuItemType
import whiz.sspark.library.data.repository.InstructorMenuRepositoryImpl
import whiz.sspark.library.utility.EventWrapper
import whiz.sspark.library.utility.toEventWrapper

class InstructorMenuViewModel(private val instructorMenuRepositoryImpl: InstructorMenuRepositoryImpl): ViewModel() {

    private val _menuLoading = MutableLiveData<Boolean>()
    private val _notificationInboxLoading = MutableLiveData<Boolean>()
    private val _calendarLoading = MutableLiveData<Boolean>()

    private val _viewLoading: MediatorLiveData<Boolean> = MediatorLiveData()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _menuResponse = MutableLiveData<EventWrapper<List<MenuDTO>>>()
    val menuResponse: LiveData<EventWrapper<List<MenuDTO>>>
        get() = _menuResponse

    private val _menuErrorResponse = MutableLiveData<EventWrapper<ApiResponseX?>>()
    val menuErrorResponse: LiveData<EventWrapper<ApiResponseX?>>
        get() = _menuErrorResponse

    private val _notificationInboxResponse = MutableLiveData<EventWrapper<MenuNotificationInboxDTO>>()
    val notificationInboxResponse: LiveData<EventWrapper<MenuNotificationInboxDTO>>
        get() = _notificationInboxResponse

    private val _notificationInboxErrorResponse = MutableLiveData<EventWrapper<ApiResponseX?>>()
    val notificationInboxErrorResponse: LiveData<EventWrapper<ApiResponseX?>>
        get() = _notificationInboxErrorResponse

    private val _calendarResponse = MutableLiveData<EventWrapper<MenuCalendarDTO>>()
    val calendarResponse: LiveData<EventWrapper<MenuCalendarDTO>>
        get() = _calendarResponse

    private val _calendarErrorResponse = MutableLiveData<EventWrapper<ApiResponseX?>>()
    val calendarErrorResponse: LiveData<EventWrapper<ApiResponseX?>>
        get() = _calendarErrorResponse

    private val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>>
        get() = _errorMessage

    init {
        val loadingObservers = listOf(_calendarLoading, _notificationInboxLoading, _menuLoading)

        loadingObservers.forEach {
            _viewLoading.addSource(it) {
                val isLoading = loadingObservers.any { it.value == true }
                _viewLoading.setValue(isLoading)
            }
        }
    }

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
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _menuResponse.value = it.toEventWrapper()
                    } ?: run {
                        _menuErrorResponse.value = it.error?.toEventWrapper()
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
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _notificationInboxResponse.value = it.toEventWrapper()
                    } ?: run {
                        _notificationInboxErrorResponse.value = it.error?.toEventWrapper()
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
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _calendarResponse.value = it.toEventWrapper()
                    } ?: run {
                        _calendarErrorResponse.value = it.error?.toEventWrapper()
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