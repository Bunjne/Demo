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
import whiz.sspark.library.data.entity.AdvisoryAppointment
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.repository.StudentAdvisoryAppointmentRepositoryImpl

class StudentAdvisoryAppointmentViewModel(private val studentAdvisoryAppointmentRepository: StudentAdvisoryAppointmentRepositoryImpl) : ViewModel() {
    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _actionLoading = MutableLiveData<Boolean>()
    val actionLoading: LiveData<Boolean>
        get() = _actionLoading

    private val _advisoryAppointmentSlotResponses = MutableLiveData<List<AdvisoryAppointment>>()
    val advisoryAppointmentSlotResponses: LiveData<List<AdvisoryAppointment>>
        get() = _advisoryAppointmentSlotResponses

    private val _advisoryAppointmentSlotErrorResponse = MutableLiveData<ApiResponseX>()
    val advisoryAppointmentSlotErrorResponse: LiveData<ApiResponseX>
        get() = _advisoryAppointmentSlotErrorResponse

    private val _reserveAdvisoryAppointmentResponse = MutableLiveData<String>()
    val reserveAdvisoryAppointmentResponse: LiveData<String>
        get() = _reserveAdvisoryAppointmentResponse

    private val _reserveAdvisoryAppointmentErrorResponse = MutableLiveData<ApiResponseX>()
    val reserveAdvisoryAppointmentErrorResponse: LiveData<ApiResponseX>
        get() = _reserveAdvisoryAppointmentErrorResponse

    private val _cancelAdvisoryAppointmentResponse = MutableLiveData<String>()
    val cancelAdvisoryAppointmentResponse: LiveData<String>
        get() = _cancelAdvisoryAppointmentResponse

    private val _cancelAdvisoryAppointmentErrorResponse = MutableLiveData<ApiResponseX>()
    val cancelAdvisoryAppointmentErrorResponse: LiveData<ApiResponseX>
        get() = _cancelAdvisoryAppointmentErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getPendingAdvisorySlot(termId: String) {
        viewModelScope.launch {
            studentAdvisoryAppointmentRepository.getPendingAdvisorySlot(termId)
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
                        _advisoryAppointmentSlotResponses.value = it
                    } ?: run {
                        _advisoryAppointmentSlotErrorResponse.value = it.error
                    }
                }
        }
    }

    fun getHistoryAdvisorySlot(termId: String) {
        viewModelScope.launch {
            studentAdvisoryAppointmentRepository.getHistoryAdvisorySlot(termId)
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
                        _advisoryAppointmentSlotResponses.value = it
                    } ?: run {
                        _advisoryAppointmentSlotErrorResponse.value = it.error
                    }
                }
        }
    }

    fun reserveAdvisorySlot(slotId: String) {
        viewModelScope.launch {
            studentAdvisoryAppointmentRepository.reserveAdvisorySlot(slotId)
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
                        _reserveAdvisoryAppointmentResponse.value = it
                    } ?: run {
                        _reserveAdvisoryAppointmentErrorResponse.value = it.error
                    }
                }
        }
    }

    fun cancelAdvisorySlot(slotId: String) {
        viewModelScope.launch {
            studentAdvisoryAppointmentRepository.cancelAdvisorySlot(slotId)
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
                        _cancelAdvisoryAppointmentResponse.value = it
                    } ?: run {
                        _cancelAdvisoryAppointmentErrorResponse.value = it.error
                    }
                }
        }
    }
}