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
import whiz.sspark.library.data.entity.PlatformOnlineClass
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.data.repository.StudentClassActivityRepositoryImpl

class StudentClassActivityViewModel(private val studentClassActivityRepository: StudentClassActivityRepositoryImpl) : ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    private val _postErrorResponse = MutableLiveData<ApiResponseX?>()
    val postErrorResponse: LiveData<ApiResponseX?>
        get() = _postErrorResponse

    private val _onlineClassesResponse = MutableLiveData<List<PlatformOnlineClass>>()
    val onlineClassesResponse: LiveData<List<PlatformOnlineClass>>
        get() = _onlineClassesResponse

    private val _onlineClassesErrorResponse = MutableLiveData<ApiResponseX>()
    val onlineClassesErrorResponse: LiveData<ApiResponseX>
        get() = _onlineClassesErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun listPosts(classGroupId: String) {
        viewModelScope.launch {
            studentClassActivityRepository.listPosts(classGroupId)
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
                        _posts.value = it
                    } ?: run {
                        _postErrorResponse.value = it.error
                    }
                }
        }
    }

    fun listOnlineClasses(classGroupId: String) {
        viewModelScope.launch {
            studentClassActivityRepository.listOnlineClasses(classGroupId)
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
                        _onlineClassesResponse.value = it
                    } ?: run {
                        _onlineClassesErrorResponse.value = it.error
                    }
                }
        }
    }
}