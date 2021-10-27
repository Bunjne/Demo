package whiz.sspark.library.data.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import whiz.sspark.library.data.repository.AdviseeAllStudentClassesRepositoryImpl

class AdviseeAllStudentClassViewModel(private val adviseeAllClassesRepository: AdviseeAllStudentClassesRepositoryImpl): StudentAllClassViewModel(adviseeAllClassesRepository) {
    fun getAllClasses(termId: String, studentId: String) {
        viewModelScope.launch {
            adviseeAllClassesRepository.getAllClass(termId, studentId)
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
                        _allClassResponse.value = it
                    } ?: run {
                        _allClassErrorResponse.value = it.error
                    }
                }
        }
    }
}