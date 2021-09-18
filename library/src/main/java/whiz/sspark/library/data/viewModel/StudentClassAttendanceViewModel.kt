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
import whiz.sspark.library.data.entity.Attendance
import whiz.sspark.library.data.repository.StudentClassAttendanceRepositoryImpl

class StudentClassAttendanceViewModel(private val studentClassAttendanceRepository: StudentClassAttendanceRepositoryImpl) : ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _classAttendanceResponse = MutableLiveData<Attendance>()
    val classAttendanceResponse: LiveData<Attendance>
        get() = _classAttendanceResponse

    private val _classAttendanceErrorResponse = MutableLiveData<ApiResponseX>()
    val classAttendanceErrorResponse: LiveData<ApiResponseX>
        get() = _classAttendanceErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getClassAttendance(classId: String) {
        viewModelScope.launch {
            studentClassAttendanceRepository.getClassAttendance(classId)
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
                        _classAttendanceResponse.value = it
                    } ?: run {
                        _classAttendanceErrorResponse.value = it.error
                    }
                }
        }
    }
}