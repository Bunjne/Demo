package whiz.sspark.library.data.viewModel

import androidx.lifecycle.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.data.entity.EventRegisteredDTO
import whiz.sspark.library.data.repository.EventRegisteredRepository
import whiz.sspark.library.data.repository.EventRegisteredRepositoryImpl
import whiz.sspark.library.utility.EventWrapper

class EventRegisteredViewModel(private val eventRegisteredRepository: EventRegisteredRepositoryImpl) : ViewModel() {

    private val _viewLoading: MutableLiveData<Boolean> = MutableLiveData()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _eventResponse = MutableLiveData<EventRegisteredDTO>()
    val eventResponse: LiveData<EventRegisteredDTO>
        get() = _eventResponse

    private val _eventErrorResponse = MutableLiveData<ApiResponseX>()
    val eventErrorResponse: LiveData<ApiResponseX>
        get() = _eventErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getRegisteredEvents() {
        viewModelScope.launch {
            eventRegisteredRepository.getRegisteredEvents()
                .onStart {
                    _viewRendering.value = null
                    _viewLoading.value = true
                }
                .onCompletion {
                    _viewLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    _viewRendering.value = it
                    val data = it.data

                    data?.let {
                        _eventResponse.value = it
                    } ?: run {
                        _eventErrorResponse.value = it.error
                    }
                }
        }
    }
}