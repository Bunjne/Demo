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
import whiz.sspark.library.data.repository.TimelineRepositoryImpl
import java.util.*

class TimelineViewModel(private val timelineRepository: TimelineRepositoryImpl) : ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _timelineResponse = MutableLiveData<TimelineResponse>()
    val timelineResponse: LiveData<TimelineResponse>
        get() = _timelineResponse

    private val _timelineErrorResponse = MutableLiveData<ApiResponseX?>()
    val timelineErrorResponse: LiveData<ApiResponseX?>
        get() = _timelineErrorResponse

    private val _todayDateResponse = MutableLiveData<Date>()
    val todayDateResponse: LiveData<Date>
        get() = _todayDateResponse

    private val _todayDateErrorResponse = MutableLiveData<ApiResponseX?>()
    val todayDateErrorResponse: LiveData<ApiResponseX?>
        get() = _todayDateErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getTimeline(date: Date,
                    differDay: Int,
                    isNetworkPreferred: Boolean) {
        viewModelScope.launch {
            timelineRepository.getTimeline(date, differDay, isNetworkPreferred)
                .onStart { _viewLoading.value = true }
                .onCompletion { _viewLoading.value = false }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _timelineResponse.value = it
                    } ?: run {
                        _timelineErrorResponse.value = it.error
                    }
                }
        }
    }

    fun getTodayDate() {
        viewModelScope.launch {
            timelineRepository.getTodayDate()
                .onStart { _viewLoading.value = true }
                .onCompletion { _viewLoading.value = false }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _todayDateResponse.value = it
                    } ?: run {
                        _todayDateErrorResponse.value = it.error
                    }
                }
        }
    }
}