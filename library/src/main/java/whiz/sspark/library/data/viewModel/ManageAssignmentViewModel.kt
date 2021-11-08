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
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.data.repository.ManageAssignmentRepositoryImpl
import java.util.*

class ManageAssignmentViewModel(private val manageAssignmentRepository: ManageAssignmentRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _manageAssignmentResponse = MutableLiveData<String>()
    val manageAssignmentResponse: LiveData<String>
        get() = _manageAssignmentResponse

    private val _manageAssignmentErrorResponse = MutableLiveData<ApiResponseX?>()
    val manageAssignmentErrorResponse: LiveData<ApiResponseX?>
        get() = _manageAssignmentErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun createAssignment(classGroupId: String, title: String, description: String, attachments: List<Attachment>, deadlineAt: Date) {
        viewModelScope.launch {
            manageAssignmentRepository.createAssignment(classGroupId, title, description, attachments, deadlineAt)
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
                        _manageAssignmentResponse.value = it
                    } ?: run {
                        _manageAssignmentErrorResponse.value = it.error
                    }
                }
        }
    }

    fun updateAssignment(classGroupId: String, assignmentId: String, title: String, description: String, attachments: List<Attachment>, deadlineAt: Date) {
        viewModelScope.launch {
            manageAssignmentRepository.updateAssignment(classGroupId, assignmentId, title, description, attachments, deadlineAt)
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
                        _manageAssignmentResponse.value = it
                    } ?: run {
                        _manageAssignmentErrorResponse.value = it.error
                    }
                }
        }
    }
}