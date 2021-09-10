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
import whiz.sspark.library.data.repository.ContactRepositoryImpl

class ContactViewModel(private val contactRepositoryImpl: ContactRepositoryImpl) : ViewModel() {

    private val _contactsLoading = MutableLiveData<Boolean>()
    val contactsLoading: LiveData<Boolean>
        get() = _contactsLoading

    private val _contactsResponse = MutableLiveData<List<Contact>>()
    val contactsResponse: LiveData<List<Contact>>
        get() = _contactsResponse

    private val _contactsErrorResponse = MutableLiveData<ApiResponseX?>()
    val contactsErrorResponse: LiveData<ApiResponseX?>
        get() = _contactsErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getContacts(isNetworkPreferred: Boolean) {
        viewModelScope.launch {
            contactRepositoryImpl.getContacts(isNetworkPreferred)
                .onStart {
                    _contactsLoading.value = true
                }
                .onCompletion {
                    _contactsLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _contactsResponse.value = it
                    }
                }
        }
    }
}