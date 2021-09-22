package whiz.sspark.library.data.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.Member
import whiz.sspark.library.data.repository.LikeBySeenByRepositoryImpl
import whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction.LikeBySeenByItemAdapter

class LikeBySeenByViewModel(private val likeBySeenByRepositoryImpl: LikeBySeenByRepositoryImpl) : ViewModel() {
    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _memberResponses = MutableLiveData<Member>()
    val memberResponses: LiveData<Member>
        get() = _memberResponses

    private val _memberErrorResponse = MutableLiveData<ApiResponseX>()
    val memberErrorResponse: LiveData<ApiResponseX>
        get() = _memberErrorResponse

    private val _userIdsResponse = MutableLiveData<List<String>>()
    val userIdsResponse: LiveData<List<String>>
        get() = _userIdsResponse

    private val _userIdsErrorResponse = MutableLiveData<ApiResponseX>()
    val userIdsErrorResponse: LiveData<ApiResponseX>
        get() = _userIdsErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getMember(classGroupId: String) {
        viewModelScope.launch {
            likeBySeenByRepositoryImpl.listClassMembers(classGroupId)
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

    fun getUserIdsByPostLiked(postId: String) {
        viewModelScope.launch {
            likeBySeenByRepositoryImpl.getUserIdsByPostLiked(postId)
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
                        _userIdsResponse.value = it
                    } ?: run {
                        _userIdsErrorResponse.value = it.error
                    }
                }
        }
    }

    fun getUserIdsByPostSeen(postId: String) {
        viewModelScope.launch {
            likeBySeenByRepositoryImpl.getUserIdsByPostSeen(postId)
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
                        _userIdsResponse.value = it
                    } ?: run {
                        _userIdsErrorResponse.value = it.error
                    }
                }
        }
    }
}