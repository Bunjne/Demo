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
import whiz.sspark.library.utility.EventWrapper
import whiz.sspark.library.utility.toEventWrapper

open class LearningPathwayViewModel(private val learningPathwayRepositoryImpl: LearningPathwayRepositoryImpl): ViewModel() {

    protected val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _courseLoading = MutableLiveData<Boolean>()
    val courseLoading: LiveData<Boolean>
        get() = _courseLoading

    protected val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    protected val _learningPathwayResponse = MutableLiveData<EventWrapper<List<LearningPathwayDTO>>>()
    val learningPathwayResponse: LiveData<EventWrapper<List<LearningPathwayDTO>>>
        get() = _learningPathwayResponse

    protected val _learningPathwayErrorResponse = MutableLiveData<EventWrapper<ApiResponseX?>>()
    val learningPathwayErrorResponse: LiveData<EventWrapper<ApiResponseX?>>
        get() = _learningPathwayErrorResponse

    private val _addCourseResponse = MutableLiveData<EventWrapper<String>>()
    val addCourseResponse: LiveData<EventWrapper<String>>
        get() = _addCourseResponse

    private val _deleteCourseResponse = MutableLiveData<EventWrapper<String>>()
    val deleteCourseResponse: LiveData<EventWrapper<String>>
        get() = _deleteCourseResponse

    private val _addCourseErrorResponse = MutableLiveData<EventWrapper<ApiResponseX?>>()
    val addCourseErrorResponse: LiveData<EventWrapper<ApiResponseX?>>
        get() = _addCourseErrorResponse

    private val _deleteCourseErrorResponse = MutableLiveData<EventWrapper<ApiResponseX?>>()
    val deleteCourseErrorResponse: LiveData<EventWrapper<ApiResponseX?>>
        get() = _deleteCourseErrorResponse

    protected val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>>
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

    fun addCourse(term: Int, academicGrade: Int, courseId: String) {
        viewModelScope.launch {
            learningPathwayRepositoryImpl.addCourse(term, academicGrade, courseId)
                .onStart {
                    _courseLoading.value = true
                }
                .onCompletion {
                    _courseLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _addCourseResponse.value = it.toEventWrapper()
                    } ?: run {
                        _addCourseErrorResponse.value = it.error.toEventWrapper()
                    }
                }
        }
    }

    fun deleteCourse(term: Int, academicGrade: Int, courseId: String) {
        viewModelScope.launch {
            learningPathwayRepositoryImpl.deleteCourse(term, academicGrade, courseId)
                .onStart {
                    _courseLoading.value = true
                }
                .onCompletion {
                    _courseLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _deleteCourseResponse.value = it.toEventWrapper()
                    } ?: run {
                        _deleteCourseErrorResponse.value = it.error.toEventWrapper()
                    }
                }
        }
    }
}