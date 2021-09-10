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
import whiz.sspark.library.data.repository.HappeningsRepositoryImpl

class HappeningsViewModel(private val happeningsRepositoryImpl: HappeningsRepositoryImpl) : ViewModel() {

    private val _newsLoading = MutableLiveData<Boolean>()
    val newsLoading: LiveData<Boolean>
        get() = _newsLoading

    private val _eventsLoading = MutableLiveData<Boolean>()
    val eventsLoading: LiveData<Boolean>
        get() = _eventsLoading

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _todayNewsResponse = MutableLiveData<List<News>>()
    val todayNewsResponse: LiveData<List<News>>
        get() = _todayNewsResponse

    private val _todayNewsErrorResponse = MutableLiveData<ApiResponseX?>()
    val todayNewsErrorResponse: LiveData<ApiResponseX?>
        get() = _todayNewsErrorResponse

    private val _eventsResponse = MutableLiveData<List<Event>>()
    val eventsResponse: LiveData<List<Event>>
        get() = _eventsResponse

    private val _eventsErrorResponse = MutableLiveData<ApiResponseX?>()
    val eventsErrorResponse: LiveData<ApiResponseX?>
        get() = _eventsErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getTodayNews() {
        viewModelScope.launch {
            happeningsRepositoryImpl.getTodayNews()
                .onStart {
                    _newsLoading.value = true
                    _viewLoading.value = true
                }
                .onCompletion {
                    _newsLoading.value = false

                    if (_newsLoading.value == false && _eventsLoading.value == false) {
                        _viewLoading.value = false
                    }
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _todayNewsResponse.value = it
                    } ?: run {
                        _todayNewsErrorResponse.value = it.error
                    }
                }
        }
    }

    fun getEvents(type: String, isNetworkPreferred: Boolean) {
        viewModelScope.launch {
            happeningsRepositoryImpl.getEvents(type, isNetworkPreferred)
                .onStart {
                    _eventsLoading.value = true
                    _viewLoading.value = true
                }
                .onCompletion {
                    _eventsLoading.value = false

                    if (_newsLoading.value == false && _eventsLoading.value == false) {
                        _viewLoading.value = false
                    }
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _eventsResponse.value = it
                    } ?: run {
                        _eventsErrorResponse.value = it.error
                    }
                }
        }
    }
}