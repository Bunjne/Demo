package whiz.sspark.library.data.viewModel

import androidx.lifecycle.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.repository.EventListRepositoryImpl
import whiz.sspark.library.utility.EventWrapper
import whiz.sspark.library.utility.toEventWrapper

class EventListViewModel(private val eventListRepository: EventListRepositoryImpl): ViewModel() {

    private val _highlightEventLoading = MutableLiveData<Boolean>()
    private val _eventLoading = MutableLiveData<Boolean>()

    private val _viewLoading: MediatorLiveData<Boolean> = MediatorLiveData()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _highlightEventResponse = MutableLiveData<EventWrapper<List<EventList>>>()
    val highlightEventResponse: LiveData<EventWrapper<List<EventList>>>
        get() = _highlightEventResponse

    private val _highlightEventErrorResponse = MutableLiveData<EventWrapper<ApiResponseX>>()
    val highlightEventErrorResponse: LiveData<EventWrapper<ApiResponseX>>
        get() = _highlightEventErrorResponse

    private val _eventResponse = MutableLiveData<EventWrapper<List<EventList>>>()
    val eventResponse: LiveData<EventWrapper<List<EventList>>>
        get() = _eventResponse

    private val _eventErrorResponse = MutableLiveData<EventWrapper<ApiResponseX>>()
    val eventErrorResponse: LiveData<EventWrapper<ApiResponseX>>
        get() = _eventErrorResponse

    private val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>>
        get() = _errorMessage

    init {
        val loadingObservers = listOf(_highlightEventLoading, _eventLoading)

        loadingObservers.forEach {
            _viewLoading.addSource(it) {
                val isLoading = loadingObservers.any { it.value == true }
                _viewLoading.setValue(isLoading)
            }
        }
    }

    fun getHighlightEvents() {
        viewModelScope.launch {
            eventListRepository.getHighlightEvents()
                .onStart {
                    _viewRendering.value = null
                    _highlightEventLoading.value = true
                }
                .onCompletion {
                    _highlightEventLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    _viewRendering.value = it
                    val data = it.data

                    data?.let {
                        _highlightEventResponse.value = convertToAdapterItem(it).toEventWrapper()
                    } ?: run {
                        _highlightEventErrorResponse.value = it.error?.toEventWrapper()
                    }
                }
        }
    }

    fun getEvent() {
        viewModelScope.launch {
            eventListRepository.getEvents()
                .onStart {
                    _viewRendering.value = null
                    _eventLoading.value = true
                }
                .onCompletion {
                    _eventLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    _viewRendering.value = it
                    val data = it.data

                    data?.let {
                        _eventResponse.value = convertToAdapterItem(it).toEventWrapper()
                    } ?: run {
                        _eventErrorResponse.value = it.error?.toEventWrapper()
                    }
                }
        }
    }

    private fun convertToAdapterItem(events: List<EventListDTO>) = events.map { it.convertToEventListItem() }
}