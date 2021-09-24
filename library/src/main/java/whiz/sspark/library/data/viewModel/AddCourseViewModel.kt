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
import whiz.sspark.library.data.entity.ConcentrateCourseDTO
import whiz.sspark.library.data.repository.AddCourseRepositoryImpl

class AddCourseViewModel(private val addCourseRepositoryImpl: AddCourseRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _concentrateCourseResponse = MutableLiveData<List<ConcentrateCourseDTO>>()
    val concentrateCourseResponse: LiveData<List<ConcentrateCourseDTO>>
        get() = _concentrateCourseResponse

    private val _concentrateCourseErrorResponse = MutableLiveData<ApiResponseX?>()
    val concentrateCourseErrorResponse: LiveData<ApiResponseX?>
        get() = _concentrateCourseErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getConcentrateCourse() {
        viewModelScope.launch {
            addCourseRepositoryImpl.getConcentrateCourse()
                .onStart {
                    _viewLoading.value = true
                }
                .onCompletion {
                    _viewLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _concentrateCourseResponse.value = it
                    } ?: run {
                        _concentrateCourseErrorResponse.value = it.error
                    }
                }
        }
    }
}