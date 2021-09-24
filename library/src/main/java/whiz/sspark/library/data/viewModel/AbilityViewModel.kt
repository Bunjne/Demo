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
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.repository.AbilityRepositoryImpl

class AbilityViewModel(private val abilityRepositoryImpl: AbilityRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _abilityResponse = MutableLiveData<List<AbilityDTO>>()
    val abilityResponse: LiveData<List<AbilityDTO>>
        get() = _abilityResponse

    private val _abilityErrorResponse = MutableLiveData<ApiResponseX?>()
    val abilityErrorResponse: LiveData<ApiResponseX?>
        get() = _abilityErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getAbility(termId: String) {
        viewModelScope.launch {
            abilityRepositoryImpl.getAbility(termId)
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
                        _abilityResponse.value = it
                    } ?: run {
                        _abilityErrorResponse.value = it.error
                    }
                }
        }
    }
}