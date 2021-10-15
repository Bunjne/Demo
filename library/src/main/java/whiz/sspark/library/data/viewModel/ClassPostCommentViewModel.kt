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
import whiz.sspark.library.data.entity.Comment
import whiz.sspark.library.data.entity.Member
import whiz.sspark.library.data.repository.ClassPostCommentRepositoryImpl

class ClassPostCommentViewModel(private val classPostCommentRepositoryImpl: ClassPostCommentRepositoryImpl) : ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _commentResponses = MutableLiveData<List<Comment>>()
    val commentResponses: LiveData<List<Comment>>
        get() = _commentResponses

    private val _commentErrorResponse = MutableLiveData<ApiResponseX>()
    val commentErrorResponse: LiveData<ApiResponseX>
        get() = _commentErrorResponse

    private val _addCommentResponse = MutableLiveData<String>()
    val addCommentResponse: LiveData<String>
        get() = _addCommentResponse

    private val _addCommentErrorResponse = MutableLiveData<ApiResponseX>()
    val addCommentErrorResponse: LiveData<ApiResponseX>
        get() = _addCommentErrorResponse

    private val _deleteCommentResponse = MutableLiveData<String>()
    val deleteCommentResponse: LiveData<String>
        get() = _deleteCommentResponse

    private val _deleteCommentErrorResponse = MutableLiveData<ApiResponseX>()
    val deleteCommentErrorResponse: LiveData<ApiResponseX>
        get() = _deleteCommentErrorResponse

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

    fun addComment(postId: String, message: String) {
        viewModelScope.launch {
            classPostCommentRepositoryImpl.addComment(postId, message)
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
                        _addCommentResponse.value = it
                    } ?: run {
                        _addCommentErrorResponse.value = it.error
                    }
                }
        }
    }

    fun deleteComment(postId: String, commentId: String) {
        viewModelScope.launch {
            classPostCommentRepositoryImpl.deleteComment(postId, commentId)
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
                        _deleteCommentResponse.value = it
                    } ?: run {
                        _deleteCommentErrorResponse.value = it.error
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