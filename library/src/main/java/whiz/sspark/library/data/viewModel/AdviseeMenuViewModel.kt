package whiz.sspark.library.data.viewModel

import androidx.lifecycle.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.repository.AdviseeMenuRepositoryImpl

class AdviseeMenuViewModel(private val adviseeMenuRepository: AdviseeMenuRepositoryImpl): ViewModel() {

    private val _adviseeInfoLoading = MutableLiveData<Boolean>()
    private val _menuLoading = MutableLiveData<Boolean>()

    private val _viewLoading: MediatorLiveData<Boolean> = MediatorLiveData()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _studentResponse = MutableLiveData<Student>()
    val studentResponse: LiveData<Student>
        get() = _studentResponse

    private val _studentErrorResponse = MutableLiveData<ApiResponseX?>()
    val studentErrorResponse: LiveData<ApiResponseX?>
        get() = _studentErrorResponse

    private val _menuResponse = MutableLiveData<List<MenuDTO>>()
    val menuResponse: LiveData<List<MenuDTO>>
        get() = _menuResponse

    private val _menuErrorResponse = MutableLiveData<ApiResponseX?>()
    val menuErrorResponse: LiveData<ApiResponseX?>
        get() = _menuErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        val loadingObservers = listOf(_adviseeInfoLoading, _menuLoading)

        loadingObservers.forEach {
            _viewLoading.addSource(it) {
                val isLoading = loadingObservers.any { it.value == true }
                _viewLoading.setValue(isLoading)
            }
        }
    }

    fun getAdviseeInfo(studentId: String) {
        viewModelScope.launch {
            adviseeMenuRepository.getAdviseeInfo(studentId)
                .onStart {
                    _adviseeInfoLoading.value = true
                }
                .onCompletion {
                    _adviseeInfoLoading.value = false
                }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _studentResponse.value = it
                    } ?: run {
                        _studentErrorResponse.value = it.error
                    }
                }
        }
    }

    fun getAdviseeMenu(studentId: String) {
        viewModelScope.launch {
            adviseeMenuRepository.getAdviseeMenu(studentId)
                .onStart {
                    _menuLoading.value = true
                }
                .onCompletion {
                    _menuLoading.value = false
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