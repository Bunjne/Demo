package whiz.sspark.library.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.repository.NotificationInboxRepositoryImpl

class NotificationInboxViewModel(private val notificationInboxRepositoryImpl: NotificationInboxRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _newNotificationResponse = MutableLiveData<List<InboxDTO>>()
    val newNotificationResponse: LiveData<List<InboxDTO>>
        get() = _newNotificationResponse

    private val _notificationResponse = MutableLiveData<List<InboxDTO>>()
    val notificationResponse: LiveData<List<InboxDTO>>
        get() = _notificationResponse

    private val _notificationErrorResponse = MutableLiveData<ApiResponseX>()
    val notificationErrorResponse: LiveData<ApiResponseX>
        get() = _notificationErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getNewNotification(page: Int, pageSize: Int) {
        viewModelScope.launch {
            notificationInboxRepositoryImpl.getNotification(page, pageSize)
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
                        _newNotificationResponse.value = it
                    } ?: run {
                        _notificationErrorResponse.value = it.error
                    }
                }
        }
    }

    fun getNotifications(page: Int, pageSize: Int) {
        viewModelScope.launch {
            notificationInboxRepositoryImpl.getNotification(page, pageSize)
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
                        _notificationResponse.value = it
                    } ?: run {
                        _notificationErrorResponse.value = it.error
                    }
                }
        }
    }
}