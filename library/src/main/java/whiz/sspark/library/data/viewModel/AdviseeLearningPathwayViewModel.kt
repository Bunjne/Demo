package whiz.sspark.library.data.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.repository.AdviseeLearningPathwayRepositoryImpl
import whiz.sspark.library.utility.toEventWrapper

class AdviseeLearningPathwayViewModel(private val adviseeLearningPathwayRepositoryImpl: AdviseeLearningPathwayRepositoryImpl): LearningPathwayViewModel(adviseeLearningPathwayRepositoryImpl) {
    fun getLearningPathway(studentId: String) {
        viewModelScope.launch {
            adviseeLearningPathwayRepositoryImpl.getLearningPathway(studentId)
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
                        _learningPathwayResponse.value = it.toEventWrapper()
                    } ?: run {
                        _learningPathwayErrorResponse.value = it.error.toEventWrapper()
                    }
                }
        }
    }
}