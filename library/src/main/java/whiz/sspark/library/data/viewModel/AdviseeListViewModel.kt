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
import whiz.sspark.library.data.entity.AbilityDTO
import whiz.sspark.library.data.entity.AdviseeListDTO
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.repository.AbilityRepositoryImpl
import whiz.sspark.library.data.repository.AdviseeListRepositoryImpl

class AdviseeListViewModel(private val adviseeListRepository: AdviseeListRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _adviseeListResponse = MutableLiveData<AdviseeListDTO>()
    val adviseeListResponse: LiveData<AdviseeListDTO>
        get() = _adviseeListResponse

    private val _adviseeListErrorResponse = MutableLiveData<ApiResponseX?>()
    val adviseeListErrorResponse: LiveData<ApiResponseX?>
        get() = _adviseeListErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getAdviseeList() {
        viewModelScope.launch {
            adviseeListRepository.getAdviseeList()
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
                        _adviseeListResponse.value = it
                    } ?: run {
                        _adviseeListErrorResponse.value = it.error
                    }
                }
        }
    }
}