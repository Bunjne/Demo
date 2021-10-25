package whiz.sspark.library.data.viewModel

import androidx.lifecycle.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.CalendarDTO
import whiz.sspark.library.data.entity.CalendarInfoDTO
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.repository.CalendarRepositoryImpl
import whiz.sspark.library.utility.EventWrapper
import whiz.sspark.library.utility.toEventWrapper

class CalendarViewModel(private val calendarRepository: CalendarRepositoryImpl): ViewModel() {

    private val _calendarLoading = MutableLiveData<Boolean>()
    private val _calendarInfoLoading = MutableLiveData<Boolean>()

    private val _viewLoading: MediatorLiveData<Boolean> = MediatorLiveData()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _calendarResponse = MutableLiveData<EventWrapper<List<CalendarDTO>>>()
    val calendarResponse: LiveData<EventWrapper<List<CalendarDTO>>>
        get() = _calendarResponse

    private val _calendarErrorResponse = MutableLiveData<EventWrapper<ApiResponseX>>()
    val calendarErrorResponse: LiveData<EventWrapper<ApiResponseX>>
        get() = _calendarErrorResponse

    private val _calendarInfoResponse = MutableLiveData<EventWrapper<List<CalendarInfoDTO>>>()
    val calendarInfoResponse: LiveData<EventWrapper<List<CalendarInfoDTO>>>
        get() = _calendarInfoResponse

    private val _calendarInfoErrorResponse = MutableLiveData<EventWrapper<ApiResponseX>>()
    val calendarInfoErrorResponse: LiveData<EventWrapper<ApiResponseX>>
        get() = _calendarInfoErrorResponse

    private val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>>
        get() = _errorMessage

    init {
        val loadingObservers = listOf(_calendarLoading, _calendarInfoLoading)

        loadingObservers.forEach {
            _viewLoading.addSource(it) {
                val isLoading = loadingObservers.any { it.value == true }
                _viewLoading.setValue(isLoading)
            }
        }
    }

    fun getCalendar(termId: String) {
        viewModelScope.launch {
            calendarRepository.getCalendar(termId)
                .onStart {
                    _viewRendering.value = null
                    _calendarLoading.value = true
                }
                .onCompletion {
                    _calendarLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    _viewRendering.value = it
                    val data = it.data

                    data?.let {
                        _calendarResponse.value = it.toEventWrapper()
                    } ?: run {
                        _calendarInfoErrorResponse.value = it.error?.toEventWrapper()
                    }
                }
        }
    }

    fun getCalendarInfo() {
        viewModelScope.launch {
            calendarRepository.getCalendarInfo()
                .onStart {
                    _viewRendering.value = null
                    _calendarInfoLoading.value = true
                }
                .onCompletion {
                    _calendarInfoLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    _viewRendering.value = it
                    val data = it.data

                    data?.let {
                        _calendarInfoResponse.value = EventWrapper(it)
                    } ?: run {
                        _calendarInfoErrorResponse.value = it.error?.toEventWrapper()
                    }
                }
        }
    }
}