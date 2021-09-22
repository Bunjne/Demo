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
import whiz.sspark.library.data.entity.ClassScheduleDTO
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.repository.ClassScheduleRepository
import whiz.sspark.library.data.repository.SchoolRecordRepositoryImpl

class ClassScheduleViewModel(private val classScheduleRepository: ClassScheduleRepository): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _classScheduleResponse = MutableLiveData<List<ClassScheduleDTO>>()
    val classScheduleResponse: LiveData<List<ClassScheduleDTO>>
        get() = _classScheduleResponse

    private val _classScheduleErrorResponse = MutableLiveData<ApiResponseX?>()
    val classScheduleErrorResponse: LiveData<ApiResponseX?>
        get() = _classScheduleErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getClassSchedule(termId: String, fromDate: String, toDate: String) {
        viewModelScope.launch {
            classScheduleRepository.getClassSchedule(termId, fromDate, toDate)
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
                        _classScheduleResponse.value = it
                    } ?: run {
                        _classScheduleErrorResponse.value = it.error
                    }
                }
        }
    }
}