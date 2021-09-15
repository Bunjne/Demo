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
import whiz.sspark.library.data.entity.Member
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.data.repository.ClassPostCommentRepositoryImpl

class ClassPostCommentViewModel(private val classPostCommentRepositoryImpl: ClassPostCommentRepositoryImpl) : ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _commentResponses = MutableLiveData<List<Post>>()
    val commentResponses: LiveData<List<Post>>
        get() = _commentResponses

    private val _commentErrorResponse = MutableLiveData<ApiResponseX>()
    val commentErrorResponse: LiveData<ApiResponseX>
        get() = _commentErrorResponse

    private val _memberResponses = MutableLiveData<Member>()
    val memberResponses: LiveData<Member>
        get() = _memberResponses

    private val _memberErrorResponse = MutableLiveData<ApiResponseX>()
    val memberErrorResponse: LiveData<ApiResponseX>
        get() = _memberErrorResponse

    private val _deletePostResponse = MutableLiveData<String>()
    val deletePostResponse: LiveData<String>
        get() = _deletePostResponse

    private val _deletePostErrorResponse = MutableLiveData<ApiResponseX>()
    val deletePostErrorResponse: LiveData<ApiResponseX>
        get() = _deletePostErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun listComments(postId: String) {
        viewModelScope.launch {
            classPostCommentRepositoryImpl.listComments(postId)
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
                        _commentResponses.value = it
                    } ?: run {
                        _commentErrorResponse.value = it.error
                    }
                }
        }
    }

    fun getMember(classId: String, isNetworkPreferred: Boolean) {
        viewModelScope.launch {
            classPostCommentRepositoryImpl.listClassMembers(classId, isNetworkPreferred)
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
                        _memberResponses.value = it
                    } ?: run {
                        _memberErrorResponse.value = it.error
                    }
                }
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            classPostCommentRepositoryImpl.deletePost(postId)
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
                        _deletePostResponse.value = it
                    } ?: run {
                        _deletePostErrorResponse.value = it.error
                    }
                }
        }
    }
}