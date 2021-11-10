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
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.repository.ContactRepositoryImpl

class ContactViewModel(private val contactRepository: ContactRepositoryImpl) : ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _contactsResponse = MutableLiveData<List<Contact>>()
    val contactsResponse: LiveData<List<Contact>>
        get() = _contactsResponse

    private val _contactsErrorResponse = MutableLiveData<ApiResponseX?>()
    val contactsErrorResponse: LiveData<ApiResponseX?>
        get() = _contactsErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getContacts() {
        viewModelScope.launch {
            contactRepository.getContacts()
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
                        _contactsResponse.value = it
                    } ?: run {
                        _contactsErrorResponse.value = it.error
                    }
                }
        }
    }
}