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
import whiz.sspark.library.data.entity.LearningOutcomeDTO
import whiz.sspark.library.data.repository.CourseSyllabusDetailRepository
import whiz.sspark.library.data.repository.CourseSyllabusDetailRepositoryImpl
import whiz.sspark.library.data.repository.ExpectOutcomeRepositoryImpl
import whiz.sspark.library.data.repository.LearningOutcomeRepositoryImpl

class CourseSyllabusDetailViewModel(private val courseSyllabusDetailViewModel: CourseSyllabusDetailRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _courseDetailResponse = MutableLiveData<ExpectOutcomeDTO>()
    val courseDetailResponse: LiveData<ExpectOutcomeDTO>
        get() = _courseDetailResponse

    private val _courseDetailErrorResponse = MutableLiveData<ApiResponseX?>()
    val courseDetailErrorResponse: LiveData<ApiResponseX?>
        get() = _courseDetailErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getCourseDetail(classGroupId: String) {
        viewModelScope.launch {
            courseSyllabusDetailViewModel.getCourseDetail(classGroupId)
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
                        _courseDetailResponse.value = it
                    } ?: run {
                        _courseDetailErrorResponse.value = it.error
                    }
                }
        }
    }
}