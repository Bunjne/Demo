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
import whiz.sspark.library.data.repository.StudentClassScheduleRepositoryImpl
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.utility.EventWrapper
import whiz.sspark.library.utility.toEventWrapper
import java.util.*

open class StudentClassScheduleViewModel(private val classScheduleRepository: StudentClassScheduleRepositoryImpl): ViewModel() {

    protected val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    protected val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _classScheduleResponse = MutableLiveData<EventWrapper<List<ClassScheduleDTO>>>()
    val classScheduleResponse: LiveData<EventWrapper<List<ClassScheduleDTO>>>
        get() = _classScheduleResponse

    private val _classScheduleErrorResponse = MutableLiveData<EventWrapper<ApiResponseX?>>()
    val classScheduleErrorResponse: LiveData<EventWrapper<ApiResponseX?>>
        get() = _classScheduleErrorResponse

    protected val _termsResponse = MutableLiveData<EventWrapper<List<Term>>>()
    val termsResponse: LiveData<EventWrapper<List<Term>>>
        get() = _termsResponse

    protected val _termsErrorResponse = MutableLiveData<EventWrapper<ApiResponseX?>>()
    val termsErrorResponse: LiveData<EventWrapper<ApiResponseX?>>
        get() = _termsErrorResponse

    protected val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>>
        get() = _errorMessage

    fun getClassSchedule(termId: String, fromDate: Date, toDate: Date) {
        val convertedFromDate = fromDate.convertToDateString(DateTimePattern.classScheduleServiceDateFormat)
        val convertedToDate = toDate.convertToDateString(DateTimePattern.classScheduleServiceDateFormat)

        viewModelScope.launch {
            classScheduleRepository.getClassSchedule(termId, convertedFromDate, convertedToDate)
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
                        _classScheduleResponse.value = it.toEventWrapper()
                    } ?: run {
                        _classScheduleErrorResponse.value = it.error.toEventWrapper()
                    }
                }
        }
    }

    fun getTerms() {
        viewModelScope.launch {
            classScheduleRepository.getTerms()
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
                        _termsResponse.value = it.toEventWrapper()
                    } ?: run {
                        _termsErrorResponse.value = it.error.toEventWrapper()
                    }
                }
        }
    }
}