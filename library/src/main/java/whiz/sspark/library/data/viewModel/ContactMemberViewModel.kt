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
import whiz.sspark.library.data.entity.StudentInstructorDTO
import whiz.sspark.library.data.repository.ContactRepositoryImpl

class ContactMemberViewModel(private val contactRepository: ContactRepositoryImpl) : ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _contactMembersResponse = MutableLiveData<List<StudentInstructorDTO>>()
    val contactsResponse: LiveData<List<StudentInstructorDTO>>
        get() = _contactMembersResponse

    private val _contactMembersErrorResponse = MutableLiveData<ApiResponseX?>()
    val contactsErrorResponse: LiveData<ApiResponseX?>
        get() = _contactMembersErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getContactMembers(contactGroupId: String) {
        viewModelScope.launch {
            contactRepository.getContactMembers(contactGroupId)
                .onStart {
                    _viewRendering.value = null
                    _viewLoading.value = true
                }
                .onCompletion {
                    _viewLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    _viewRendering.value = it
                    val data = it.data

                    data?.let {
                        _contactMembersResponse.value = it
                    } ?: run {
                        _contactMembersErrorResponse.value = it.error
                    }
                }
        }
    }
}