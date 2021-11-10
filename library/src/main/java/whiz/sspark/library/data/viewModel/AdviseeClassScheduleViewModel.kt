package whiz.sspark.library.data.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.repository.AdviseeClassScheduleRepositoryImpl
import whiz.sspark.library.utility.toEventWrapper

class AdviseeClassScheduleViewModel(private val adviseeClassScheduleRepository: AdviseeClassScheduleRepositoryImpl): StudentClassScheduleViewModel(adviseeClassScheduleRepository) {
    fun getTerms(studentId: String) {
        viewModelScope.launch {
            adviseeClassScheduleRepository.getTerms(studentId)
                .onStart {
                    _viewLoading.value = true
                    _viewRendering.value = null
                }
                .onCompletion { _viewLoading.value = false }
                .catch {
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    _viewRendering.value = it
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