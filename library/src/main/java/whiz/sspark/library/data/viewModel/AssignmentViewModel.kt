package whiz.sspark.library.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.data.entity.AssignmentDTO
import whiz.sspark.library.data.entity.ConcentrateCourseDTO
import whiz.sspark.library.data.repository.AddCourseRepositoryImpl
import whiz.sspark.library.data.repository.AssignmentRepositoryImpl

class AssignmentViewModel(private val assignmentRepository: AssignmentRepositoryImpl): ViewModel() {

    private val _assignmentResponse = MutableLiveData<PagingData<Assignment>>()
    val assignmentResponse: LiveData<PagingData<Assignment>>
        get() = _assignmentResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getAssignment() {
        viewModelScope.launch {
            assignmentRepository.getAssignment()
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .map {
                    it.map {
                        Assignment(
                            color = it.classGroup.colorCode1,
                            courseName = it.classGroup.name,
                            courseCode = it.classGroup.code,
                            createdAt = it.createdAt,
                            updatedAt = it.updatedAt,
                            deadlineAt = it.deadlineAt,
                            title = it.title,
                            description = it.message,
                            instructorName = it.instructor.fullName,
                            imageUrl = it.instructor.imageUrl,
                            gender = it.instructor.gender,
                            attachments = it.attachments
                        )
                    }
                }
                .collectLatest {
                    _assignmentResponse.value = it
                }
        }
    }
}