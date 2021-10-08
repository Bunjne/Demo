package whiz.sspark.library.data.viewModel

import androidx.lifecycle.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.MenuItemType
import whiz.sspark.library.data.repository.StudentMenuRepositoryImpl

class StudentMenuViewModel(private val studentMenuRepository: StudentMenuRepositoryImpl): ViewModel() {

    private val _menuLoading = MutableLiveData<Boolean>()
    private val _notificationInboxLoading = MutableLiveData<Boolean>()
    private val _calendarLoading = MutableLiveData<Boolean>()
    private val _gradeSummaryLoading = MutableLiveData<Boolean>()
    private val _advisingLoading = MutableLiveData<Boolean>()

    private val _viewLoading: MediatorLiveData<Boolean> = MediatorLiveData()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _menuResponse = MutableLiveData<List<MenuDTO>>()
    val menuResponse: LiveData<List<MenuDTO>>
        get() = _menuResponse

    private val _menuErrorResponse = MutableLiveData<ApiResponseX?>()
    val menuErrorResponse: LiveData<ApiResponseX?>
        get() = _menuErrorResponse

    private val _advisingNoteResponse = MutableLiveData<MenuAdvisingNoteDTO>()
    val advisingNoteResponse: LiveData<MenuAdvisingNoteDTO>
        get() = _advisingNoteResponse

    private val _advisingNoteErrorResponse = MutableLiveData<ApiResponseX?>()
    val advisingNoteErrorResponse: LiveData<ApiResponseX?>
        get() = _advisingNoteErrorResponse

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

    private val _gradeSummaryResponse = MutableLiveData<List<MenuGradeSummaryDTO>>()
    val gradeSummaryResponse: LiveData<List<MenuGradeSummaryDTO>>
        get() = _gradeSummaryResponse

    private val _gradeSummaryErrorResponse = MutableLiveData<ApiResponseX?>()
    val gradeSummaryErrorResponse: LiveData<ApiResponseX?>
        get() = _gradeSummaryErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getMenu() {
        viewModelScope.launch {
            studentMenuRepository.getMenu()
                .onStart {
                    _menuLoading.value = true
                }
                .onCompletion {
                    _menuLoading.value = false
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

    private fun getAdvising() {
        viewModelScope.launch {
            studentMenuRepository.getAdvisingNote()
                .onStart {
                    _advisingLoading.value = true
                }
                .onCompletion {
                    _advisingLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _advisingNoteResponse.value = it
                    } ?: run {
                        _advisingNoteErrorResponse.value = it.error
                    }
                }
        }
    }

    private fun getNotificationInbox() {
        viewModelScope.launch {
            studentMenuRepository.getNotificationInbox()
                .onStart {
                    _notificationInboxLoading.value = true
                }
                .onCompletion {
                    _notificationInboxLoading.value = false
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
            studentMenuRepository.getCalendar()
                .onStart {
                    _calendarLoading.value = true
                }
                .onCompletion {
                    _calendarLoading.value = false
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

    private fun getGradeSummary(termId: String) {
        viewModelScope.launch {
            studentMenuRepository.getGradeSummary(termId)
                .onStart {
                    _gradeSummaryLoading.value = true
                }
                .onCompletion {
                    _gradeSummaryLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _gradeSummaryResponse.value = it
                    } ?: run {
                        _gradeSummaryErrorResponse.value = it.error
                    }
                }
        }
    }

    fun fetchWidget(menusDTO: List<MenuDTO>, termId: String) {
        menusDTO.forEach {
            it.items.forEach {
                when (it.type) {
                    MenuItemType.ADVISING_WIDGET.type -> getAdvising()
                    MenuItemType.NOTIFICATION_WIDGET.type -> getNotificationInbox()
                    MenuItemType.CALENDAR_WIDGET.type -> getCalendar()
                    MenuItemType.GRADE_SUMMARY.type -> getGradeSummary(termId)
                }
            }
        }
    }
}