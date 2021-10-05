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
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.ExamScheduleDTO
import whiz.sspark.library.data.repository.StudentExamScheduleRepositoryImpl

class StudentExamScheduleViewModel(private val examScheduleRepository: StudentExamScheduleRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _examScheduleResponse = MutableLiveData<ExamScheduleDTO>()
    val examScheduleResponse: LiveData<ExamScheduleDTO>
        get() = _examScheduleResponse

    private val _examScheduleErrorResponse = MutableLiveData<ApiResponseX?>()
    val examScheduleErrorResponse: LiveData<ApiResponseX?>
        get() = _examScheduleErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getClassSchedule(termId: String) {
        viewModelScope.launch {
            examScheduleRepository.getExamSchedule(termId)
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
                        _examScheduleResponse.value = it
                    } ?: run {
                        _examScheduleErrorResponse.value = it.error
                    }
                }
        }
    }
}