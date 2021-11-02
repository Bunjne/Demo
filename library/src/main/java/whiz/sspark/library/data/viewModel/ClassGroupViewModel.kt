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
import whiz.sspark.library.data.entity.ClassGroup
import whiz.sspark.library.data.repository.ClassGroupRepositoryImpl

class ClassGroupViewModel(private val classGroupRepository: ClassGroupRepositoryImpl) : ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _classGroupResponse = MutableLiveData<ClassGroup>()
    val classGroupResponse: LiveData<ClassGroup>
        get() = _classGroupResponse

    private val _classGroupErrorResponse = MutableLiveData<ApiResponseX>()
    val classGroupErrorResponse: LiveData<ApiResponseX>
        get() = _classGroupErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getClassGroups() {
        viewModelScope.launch {
            classGroupRepository.getClassGroup()
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
                        _classGroupResponse.value = it
                    } ?: run {
                        _classGroupErrorResponse.value = it.error
                    }
                }
        }
    }
}