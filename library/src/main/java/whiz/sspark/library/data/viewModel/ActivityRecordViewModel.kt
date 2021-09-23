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
import whiz.sspark.library.data.entity.ActivityDTO
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.repository.ActivityRecordRepositoryImpl

class ActivityRecordViewModel(private val activityRecordRepositoryImpl: ActivityRecordRepositoryImpl): ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    private val _viewRendering = MutableLiveData<DataWrapperX<Any>>()
    val viewRendering: LiveData<DataWrapperX<Any>>
        get() = _viewRendering

    private val _activityRecordResponse = MutableLiveData<List<ActivityDTO>>()
    val activityRecordResponse: LiveData<List<ActivityDTO>>
        get() = _activityRecordResponse

    private val _activityRecordErrorResponse = MutableLiveData<ApiResponseX?>()
    val activityRecordErrorResponse: LiveData<ApiResponseX?>
        get() = _activityRecordErrorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getActivityRecord(termId: String) {
        viewModelScope.launch {
            activityRecordRepositoryImpl.getActivityRecord(termId)
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
                        _activityRecordResponse.value = it
                    } ?: run {
                        _activityRecordErrorResponse.value = it.error
                    }
                }
        }
    }
}