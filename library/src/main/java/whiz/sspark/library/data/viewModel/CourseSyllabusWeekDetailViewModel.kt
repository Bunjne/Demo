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
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.repository.*

class CourseSyllabusWeekDetailViewModel(private val courseSyllabusWeekDetailViewModel: CourseSyllabusWeekDetailRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _courseWeekDetailResponse = MutableLiveData<CourseSyllabusDTO>()
    val courseWeekDetailResponse: LiveData<CourseSyllabusDTO>
        get() = _courseWeekDetailResponse

    private val _courseWeekDetailErrorResponse = MutableLiveData<ApiResponseX?>()
    val courseWeekDetailErrorResponse: LiveData<ApiResponseX?>
        get() = _courseWeekDetailErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getCourseWeekDetail(classGroupId: String, termId: String) {
        viewModelScope.launch {
            courseSyllabusWeekDetailViewModel.getCourseWeekDetail(classGroupId, termId)
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
                        _courseWeekDetailResponse.value = it
                    } ?: run {
                        _courseWeekDetailErrorResponse.value = it.error
                    }
                }
        }
    }
}