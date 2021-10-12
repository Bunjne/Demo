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
import whiz.sspark.library.data.repository.AdviseeSchoolRecordRepositoryImpl
import whiz.sspark.library.data.repository.SchoolRecordRepositoryImpl

class AdviseeSchoolRecordViewModel(private val adviseeSchoolRecordRepositoryImpl: AdviseeSchoolRecordRepositoryImpl): SchoolRecordViewModel(adviseeSchoolRecordRepositoryImpl) {
    fun getTerms(studentId: String) {
        viewModelScope.launch {
            adviseeSchoolRecordRepositoryImpl.getTerms(studentId)
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
                        _termsResponse.value = it
                    } ?: run {
                        _termsErrorResponse.value = it.error
                    }
                }
        }
    }
}