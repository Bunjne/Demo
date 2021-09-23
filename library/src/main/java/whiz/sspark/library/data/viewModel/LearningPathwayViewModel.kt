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
import whiz.sspark.library.data.entity.LearningPathwayDTO
import whiz.sspark.library.data.repository.LearningPathwayRepositoryImpl

class LearningPathwayViewModel(private val learningPathwayRepositoryImpl: LearningPathwayRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _courseLoading = MutableLiveData<Boolean>()
    val courseLoading: LiveData<Boolean>
        get() = _courseLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _learningPathwayResponse = MutableLiveData<List<LearningPathwayDTO>>()
    val learningPathwayResponse: LiveData<List<LearningPathwayDTO>>
        get() = _learningPathwayResponse

    private val _learningPathwayErrorResponse = MutableLiveData<ApiResponseX?>()
    val learningPathwayErrorResponse: LiveData<ApiResponseX?>
        get() = _learningPathwayErrorResponse

    private val _addCourseResponse = MutableLiveData<String>()
    val addCourseResponse: LiveData<String>
        get() = _addCourseResponse

    private val _deleteCourseResponse = MutableLiveData<String>()
    val deleteCourseResponse: LiveData<String>
        get() = _deleteCourseResponse

    private val _addCourseErrorResponse = MutableLiveData<ApiResponseX?>()
    val addCourseErrorResponse: LiveData<ApiResponseX?>
        get() = _addCourseErrorResponse

    private val _deleteCourseErrorResponse = MutableLiveData<ApiResponseX?>()
    val deleteCourseErrorResponse: LiveData<ApiResponseX?>
        get() = _deleteCourseErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getLearningPathway() {
        viewModelScope.launch {
            learningPathwayRepositoryImpl.getLearningPathway()
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
                        _learningPathwayResponse.value = it
                    } ?: run {
                        _learningPathwayErrorResponse.value = it.error
                    }
                }
        }
    }

    fun addCourse(termId: String, courseId: String) {
        viewModelScope.launch {
            learningPathwayRepositoryImpl.addCourse(termId, courseId)
                .onStart {
                    _courseLoading.value = true
                }
                .onCompletion {
                    _courseLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _addCourseResponse.value = it
                    } ?: run {
                        _addCourseErrorResponse.value = it.error
                    }
                }
        }
    }

    fun deleteCourse(termId: String, courseId: String) {
        viewModelScope.launch {
            learningPathwayRepositoryImpl.deleteCourse(termId, courseId)
                .onStart {
                    _courseLoading.value = true
                }
                .onCompletion {
                    _courseLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _deleteCourseResponse.value = it
                    } ?: run {
                        _deleteCourseErrorResponse.value = it.error
                    }
                }
        }
    }
}