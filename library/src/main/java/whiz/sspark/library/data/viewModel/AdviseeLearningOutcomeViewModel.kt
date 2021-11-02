package whiz.sspark.library.data.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.repository.AdviseeLearningOutcomeRepositoryImpl

class AdviseeLearningOutcomeViewModel(private val adviseeLearningOutcomeRepository: AdviseeLearningOutcomeRepositoryImpl): LearningOutcomeViewModel(adviseeLearningOutcomeRepository) {

    fun getLearningOutcome(termId: String, studentId: String) {
        viewModelScope.launch {
            adviseeLearningOutcomeRepository.getLearningOutcome(termId, studentId)
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
                        _learningOutcomeResponse.value = it
                    } ?: run {
                        _learningOutcomeErrorResponse.value = it.error
                    }
                }
        }
    }
}