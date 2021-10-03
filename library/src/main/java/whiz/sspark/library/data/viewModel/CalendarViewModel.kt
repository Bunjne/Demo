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
import whiz.sspark.library.utility.LiveDataEvent

class CalendarViewModel(private val calendarRepository: CalendarRepositoryImpl): ViewModel() {

    private val _calendarLoading = MutableLiveData<Boolean>()
    private val _calendarInfoLoading = MutableLiveData<Boolean>()
    val viewLoading: MediatorLiveData<Boolean> = MediatorLiveData()

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _calendarResponse = MutableLiveData<LiveDataEvent<List<CalendarDTO>>>()
    val calendarResponse: LiveData<LiveDataEvent<List<CalendarDTO>>>
        get() = _calendarResponse

    private val _calendarErrorResponse = MutableLiveData<ApiResponseX?>()
    val calendarErrorResponse: LiveData<ApiResponseX?>
        get() = _calendarErrorResponse

    private val _calendarInfoResponse = MutableLiveData<LiveDataEvent<List<CalendarInfoDTO>>>()
    val calendarInfoResponse: LiveData<LiveDataEvent<List<CalendarInfoDTO>>>
        get() = _calendarInfoResponse

    private val _calendarInfoErrorResponse = MutableLiveData<ApiResponseX?>()
    val calendarInfoErrorResponse: LiveData<ApiResponseX?>
        get() = _calendarInfoErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        val loadingObservers = listOf(_calendarLoading, _calendarInfoLoading)

        loadingObservers.forEach {
            viewLoading.addSource(it) {
                val isLoading = loadingObservers.any { it.value == true }
                viewLoading.setValue(isLoading)
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
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    _viewRendering.value = it
                    val data = it.data

                    data?.let {
                        _calendarResponse.value = LiveDataEvent(it)
                    } ?: run {
                        _calendarErrorResponse.value = it.error
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
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    _viewRendering.value = it
                    val data = it.data

                    data?.let {
                        _calendarInfoResponse.value = LiveDataEvent(it)
                    } ?: run {
                        _calendarInfoErrorResponse.value = it.error
                    }
                }
        }
    }
}