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
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.repository.SchoolRecordRepositoryImpl
import whiz.sspark.library.utility.EventWrapper
import whiz.sspark.library.utility.toEventWrapper

class SchoolRecordViewModel(private val schoolRecordRepositoryImpl: SchoolRecordRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _termsResponse = MutableLiveData<EventWrapper<List<Term>>>()
    val termsResponse: LiveData<EventWrapper<List<Term>>>
        get() = _termsResponse

    private val _termsErrorResponse = MutableLiveData<EventWrapper<ApiResponseX?>>()
    val termsErrorResponse: LiveData<EventWrapper<ApiResponseX?>>
        get() = _termsErrorResponse

    private val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>>
        get() = _errorMessage

    fun getTerms() {
        viewModelScope.launch {
            schoolRecordRepositoryImpl.getTerms()
                .onStart {
                    _viewLoading.value = true
                }
                .onCompletion {
                    _viewLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage?.toEventWrapper()
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _termsResponse.value = it.toEventWrapper()
                    } ?: run {
                        _termsErrorResponse.value = it.error.toEventWrapper()
                    }
                }
        }
    }
}