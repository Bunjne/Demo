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
import whiz.sspark.library.data.repository.ClassMemberRepositoryImpl

class ClassMemberViewModel(private val classMemberRepository: ClassMemberRepositoryImpl) : ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _memberResponse = MutableLiveData<Member>()
    val memberResponse: LiveData<Member>
        get() = _memberResponse

    private val _memberErrorResponse = MutableLiveData<ApiResponseX>()
    val memberErrorResponse: LiveData<ApiResponseX>
        get() = _memberErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getClassMember(classId: String, isNetworkPreferred: Boolean) {
        viewModelScope.launch {
            classMemberRepository.getClassMember(classId, isNetworkPreferred)
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
                        _memberResponse.value = it
                    } ?: run {
                        _memberErrorResponse.value = it.error
                    }
                }
        }
    }
}