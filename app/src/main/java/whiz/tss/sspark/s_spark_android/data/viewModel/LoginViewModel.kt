package whiz.tss.sspark.s_spark_android.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.repository.ProfileRepositoryImpl
import whiz.tss.sspark.s_spark_android.data.repository.LoginRepositoryImpl

class LoginViewModel(private val loginRepository: LoginRepositoryImpl,
                     private val profileRepository: ProfileRepositoryImpl) : ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _loginResponse = MutableLiveData<AuthenticationInformation>()
    val loginResponse: LiveData<AuthenticationInformation>
        get() = _loginResponse

    private val _loginErrorResponse = MutableLiveData<ApiResponseX?>()
    val loginErrorResponse: LiveData<ApiResponseX?>
        get() = _loginErrorResponse

    private val _profileResponse = MutableLiveData<Student>()
    val profileResponse: LiveData<Student>
        get() = _profileResponse

    private val _profileErrorResponse = MutableLiveData<ApiResponseX?>()
    val profileErrorResponse: LiveData<ApiResponseX?>
        get() = _profileErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun login(username: String, password: String, uuid: String, operatorName: String) {
        viewModelScope.launch {
            loginRepository.login(username, password, uuid, operatorName)
                .onStart { _viewLoading.value = true }
                .onCompletion { _viewLoading.value = false }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _loginResponse.value = it
                    } ?: run {
                        _loginErrorResponse.value = it.error
                    }
                }
        }
    }

    fun getStudentProfile() {
        viewModelScope.launch {
            profileRepository.getStudentProfile()
                .onStart { _viewLoading.value = true }
                .onCompletion { _viewLoading.value = false }
                .catch {
                    _errorMessage.value = it.localizedMessage
                }
                .collect {
                    val data = it.data

                    data?.let {
                        _profileResponse.value = it
                    } ?: run {
                        _profileErrorResponse.value = it.error
                    }
                }
        }
    }
}