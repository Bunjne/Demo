package whiz.sspark.library.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.repository.InstructorClassAssignmentDetailRepositoryImpl
import whiz.sspark.library.utility.EventWrapper
import whiz.sspark.library.utility.toEventWrapper

class InstructorClassAssignmentDetailViewModel(private val instructorClassAssignmentDetailRepository: InstructorClassAssignmentDetailRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _deleteAssignmentResponse = MutableLiveData<EventWrapper<String>>()
    val deleteAssignmentResponse: LiveData<EventWrapper<String>>
        get() = _deleteAssignmentResponse

    private val _deleteAssignmentErrorResponse = MutableLiveData<EventWrapper<ApiResponseX>>()
    val deleteAssignmentErrorResponse: LiveData<EventWrapper<ApiResponseX>>
        get() = _deleteAssignmentErrorResponse

    private val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>>
        get() = _errorMessage

    fun deleteAssignment(classGroupId: String, assignmentId: String) {
        viewModelScope.launch {
            instructorClassAssignmentDetailRepository.deleteAssignment(classGroupId, assignmentId)
                .onStart {
                    _viewLoading.value = true
                }
                .onCompletion {
                    _viewLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _deleteAssignmentResponse.value = it.toEventWrapper()
                    } ?: run {
                        _deleteAssignmentErrorResponse.value = it.error?.toEventWrapper()
                    }
                }
        }
    }
}