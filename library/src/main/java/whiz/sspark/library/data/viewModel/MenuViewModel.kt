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
import whiz.sspark.library.data.entity.MenuDTO
import whiz.sspark.library.data.repository.MenuRepositoryImpl

class MenuViewModel(private val menuRepositoryImpl: MenuRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _menuResponse = MutableLiveData<List<MenuDTO>>()
    val menuResponse: LiveData<List<MenuDTO>>
        get() = _menuResponse

    private val _menuErrorResponse = MutableLiveData<ApiResponseX?>()
    val menuErrorResponse: LiveData<ApiResponseX?>
        get() = _menuErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getMenu() {
        viewModelScope.launch {
            menuRepositoryImpl.getMenu()
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
                        _menuResponse.value = it
                    } ?: run {
                        _menuErrorResponse.value = it.error
                    }
                }
        }
    }
}