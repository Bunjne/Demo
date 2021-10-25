package whiz.sspark.library.data.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.repository.AdviseeExpectOutcomeRepositoryImpl

class AdviseeExpectOutcomeViewModel(private val adviseeExpectOutcomeRepository: AdviseeExpectOutcomeRepositoryImpl): ExpectOutcomeViewModel(adviseeExpectOutcomeRepository) {

    fun getExpectOutcome(courseId: String, termId: String, studentId: String) {
        viewModelScope.launch {
            adviseeExpectOutcomeRepository.getExpectOutcome(courseId, termId, studentId)
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
                        _expectOutcomeResponse.value = it
                    } ?: run {
                        _expectOutcomeErrorResponse.value = it.error
                    }
                }
        }
    }
}