package whiz.sspark.library.data.viewModel

import android.content.DialogInterface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import whiz.sspark.library.data.repository.StudentClassActivityRepositoryImpl

class StudentClassActivityViewModel(private val studentClassActivityRepository: StudentClassActivityRepositoryImpl) : ViewModel() {

    private val _viewLoading = MutableLiveData<Boolean>()
    val viewLoading: LiveData<Boolean>
        get() = _viewLoading

    val posts = MutableLiveData<List<Post>>()
    val postErrorResponse = MutableLiveData<ApiErrorResponse>()

    val onlineClassesResponse = MutableLiveData<List<PlatformOnlineClass>>()
    val onlineClassesErrorResponse = MutableLiveData<ApiErrorResponse>()

    fun listPosts(id: Long, isNetworkPreferred: Boolean) {
        state.postLoading.value = true

        classCollaborationRepository.listPosts(id, isNetworkPreferred).observeForever {
            state.postLoading.value = false

            val data = it?.data
            data?.let {
                posts.value = it
            } ?: {
                postErrorResponse.value = it?.error
            }()
        }
    }

    fun listOnlineClasses(classId: Long) {
        state.onlineClassesLoading.value = true
        classCollaborationRepository.listOnlineClasses(classId).observeForever {
            state.onlineClassesLoading.value = false

            val data = it.data
            data?.let {
                onlineClassesResponse.value = it
            } ?: {
                onlineClassesErrorResponse.value = it.error
            }()
        }
    }

    fun getMSTeamsUrl(sectionId: Long, handleFailedGenerateMSTeamsUrl: () -> Unit) {
        state.getMSTeamsURLLoading.value = true
        classCollaborationRepository.getMSTeamsUrl(sectionId).observeForever {
            state.getMSTeamsURLLoading.value = false

            val data = it.data
            data?.let {
                getMSTeamsURLResponse.value = it

                if (it.onlineClassUrl.isNullOrBlank()) {
                    handleFailedGenerateMSTeamsUrl()
                }
            } ?: {
                getMSTeamsURLErrorResponse.value = it.error
                handleFailedGenerateMSTeamsUrl()
            }()
        }
    }
}