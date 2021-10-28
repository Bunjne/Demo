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
import whiz.sspark.library.data.repository.AdviseeActivityRecordRepositoryImpl

class AdviseeActivityRecordViewModel(private val adviseeActivityRecordRepository: AdviseeActivityRecordRepositoryImpl): ActivityRecordViewModel(adviseeActivityRecordRepository) {

    fun getActivityRecord(termId: String, studentId: String) {
        viewModelScope.launch {
            adviseeActivityRecordRepository.getActivityRecord(termId, studentId)
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