package whiz.sspark.library.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.AssignmentDTO
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.repository.AssignmentRepositoryImpl
import whiz.sspark.library.utility.EventWrapper
import whiz.sspark.library.utility.toEventWrapper

class AssignmentViewModel(private val assignmentRepository: AssignmentRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _latestAssignmentResponse = MutableLiveData<EventWrapper<AssignmentDTO>>()
    val latestAssignmentResponse: LiveData<EventWrapper<AssignmentDTO>>
        get() = _latestAssignmentResponse

    private val _assignmentResponse = MutableLiveData<EventWrapper<AssignmentDTO>>()
    val assignmentResponse: LiveData<EventWrapper<AssignmentDTO>>
        get() = _assignmentResponse

    private val _assignmentErrorResponse = MutableLiveData<EventWrapper<ApiResponseX>>()
    val assignmentErrorResponse: LiveData<EventWrapper<ApiResponseX>>
        get() = _assignmentErrorResponse

    private val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>>
        get() = _errorMessage

    fun getLatestAssignments(termId: String, page: Int, pageSize: Int) {
        viewModelScope.launch {
            assignmentRepository.getAssignment(termId, page, pageSize)
                .onStart {
                    _viewRendering.value = null
                    _viewLoading.value = true
                }
                .onCompletion {
                    _viewLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    _viewRendering.value = it

                    val data = it.data

                    data?.let {
                        _latestAssignmentResponse.value = it.toEventWrapper()
                    } ?: run {
                        _assignmentErrorResponse.value = it.error?.toEventWrapper()
                    }
                }
        }
    }

    fun getAssignments(termId: String, page: Int, pageSize: Int) {
        viewModelScope.launch {
            assignmentRepository.getAssignment(termId, page, pageSize)
                .onStart {
                    _viewRendering.value = null
                    _viewLoading.value = true
                }
                .onCompletion {
                    _viewLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    _viewRendering.value = it

                    val data = it.data

                    data?.let {
                        _assignmentResponse.value = it.toEventWrapper()
                    } ?: run {
                        _assignmentErrorResponse.value = it.error?.toEventWrapper()
                    }
                }
        }
    }
}