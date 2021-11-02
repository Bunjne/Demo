package whiz.sspark.library.data.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.repository.InstructorClassScheduleRepositoryImpl
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.utility.toEventWrapper
import java.util.*

class InstructorClassScheduleViewModel(private val instructorClassScheduleRepository: InstructorClassScheduleRepositoryImpl): StudentClassScheduleViewModel(instructorClassScheduleRepository) {

    override fun getClassSchedule(termId: String, fromDate: Date, toDate: Date) {
        val convertedFromDate = fromDate.convertToDateString(DateTimePattern.classScheduleServiceDateFormat)
        val convertedToDate = toDate.convertToDateString(DateTimePattern.classScheduleServiceDateFormat)

        viewModelScope.launch {
            instructorClassScheduleRepository.getClassSchedule(termId, convertedFromDate, convertedToDate)
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

    override fun getTerms() {
        viewModelScope.launch {
            instructorClassScheduleRepository.getTerms()
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