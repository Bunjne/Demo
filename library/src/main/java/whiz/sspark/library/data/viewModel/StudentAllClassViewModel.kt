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
import whiz.sspark.library.data.entity.ClassScheduleAllClassDTO
import whiz.sspark.library.data.repository.StudentAllClassRepositoryImpl

class StudentAllClassViewModel(private val studentAllClassRepository: StudentAllClassRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _allClassResponse = MutableLiveData<List<ClassScheduleAllClassDTO>>()
    val allClassResponse: LiveData<List<ClassScheduleAllClassDTO>>
        get() = _allClassResponse

    private val _allClassErrorResponse = MutableLiveData<ApiResponseX?>()
    val allClassErrorResponse: LiveData<ApiResponseX?>
        get() = _allClassErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getAllClasses(termId: String) {
        viewModelScope.launch {
            studentAllClassRepository.getAllClass(termId)
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
                        _allClassResponse.value = it
                    } ?: run {
                        _allClassErrorResponse.value = it.error
                    }
                }
        }
    }
}