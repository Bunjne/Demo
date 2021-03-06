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
import whiz.sspark.library.data.entity.LearningOutcomeDTO
import whiz.sspark.library.data.repository.LearningOutcomeRepositoryImpl

open class LearningOutcomeViewModel(private val learningOutcomeRepositoryImpl: LearningOutcomeRepositoryImpl): ViewModel() {

    protected val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    protected val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    protected val _learningOutcomeResponse = MutableLiveData<List<LearningOutcomeDTO>>()
    val learningOutcomeResponse: LiveData<List<LearningOutcomeDTO>>
        get() = _learningOutcomeResponse

    protected val _learningOutcomeErrorResponse = MutableLiveData<ApiResponseX?>()
    val learningOutcomeErrorResponse: LiveData<ApiResponseX?>
        get() = _learningOutcomeErrorResponse

    protected val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    open fun getLearningOutcome(termId: String) {
        viewModelScope.launch {
            learningOutcomeRepositoryImpl.getLearningOutcome(termId)
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