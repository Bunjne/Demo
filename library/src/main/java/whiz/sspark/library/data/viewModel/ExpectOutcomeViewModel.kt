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
import whiz.sspark.library.data.entity.ExpectOutcomeDTO
import whiz.sspark.library.data.repository.ExpectOutcomeRepositoryImpl

open class ExpectOutcomeViewModel(private val learningOutcomeRepositoryImpl: ExpectOutcomeRepositoryImpl): ViewModel() {

    protected val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    protected val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    protected val _expectOutcomeResponse = MutableLiveData<ExpectOutcomeDTO>()
    val expectOutcomeResponse: LiveData<ExpectOutcomeDTO>
        get() = _expectOutcomeResponse

    protected val _expectOutcomeErrorResponse = MutableLiveData<ApiResponseX?>()
    val expectOutcomeErrorResponse: LiveData<ApiResponseX?>
        get() = _expectOutcomeErrorResponse

    protected val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    open fun getExpectOutcome(courseId: String, termId: String) {
        viewModelScope.launch {
            learningOutcomeRepositoryImpl.getExpectOutcome(courseId, termId)
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