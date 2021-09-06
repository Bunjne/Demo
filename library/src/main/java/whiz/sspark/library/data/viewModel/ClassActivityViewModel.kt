package whiz.sspark.library.data.viewModel

import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import whiz.u.library.data.entity.*
import whiz.u.library.data.repository.impl.ClassCollaborationRepositoryImpl
import whiz.u.library.data.repository.impl.ClassMemberRepositoryImpl

class ClassActivityViewModel(private val classCollaborationRepository: ClassCollaborationRepositoryImpl,
                             private val classMemberRepository: ClassMemberRepositoryImpl) : ViewModel() {

    val state = State(
        postLoading = MutableLiveData(),
        deletePostLoading = MutableLiveData(),
        onlineClassLoading = MutableLiveData(),
        enableOnlineClassLoading = MutableLiveData(),
        saveOnlineClassLoading = MutableLiveData(),
        getMSTeamsURLLoading = MutableLiveData(),
        onlineClassesLoading = MutableLiveData()
    )

    val posts = MutableLiveData<List<Post>>()
    val postErrorResponse = MutableLiveData<ApiErrorResponse>()

    val postErrorResponseV3 = MutableLiveData<ApiResponseX>()

    val postInteractionUsers = MutableLiveData<List<String>>()
    val postInteractionUsersErrorResponse = MutableLiveData<ApiErrorResponse>()

    val postInteractionUsersErrorResponseV3 = MutableLiveData<ApiResponseX>()

    val classMembers = MutableLiveData<Member>()
    val classMembersErrorResponse = MutableLiveData<ApiErrorResponse>()

    val classMemberErrorResponseV3 = MutableLiveData<ApiResponseX>()

    val onlineClassesResponse = MutableLiveData<List<PlatformOnlineClass>>()
    val onlineClassesErrorResponse = MutableLiveData<ApiErrorResponse>()

    val onlineClassesErrorResponseV3 = MutableLiveData<ApiResponseX>()

    val getMSTeamsURLResponse = MutableLiveData<OnlineClass>()
    val getMSTeamsURLErrorResponse = MutableLiveData<ApiErrorResponse>()

    val getMSTeamsURLErrorResponseV3 = MutableLiveData<ApiResponseX>()

    val getSharedClassSectionsResponse = MutableLiveData<List<ClassSharedSection>>()
    val getSharedClassSectionsErrorResponse = MutableLiveData<ApiResponseX>()

    val saveOnlineClassResponse = MutableLiveData<String>()
    val saveOnlineClassErrorResponse = MutableLiveData<ApiErrorResponse>()

    val saveOnlineClassErrorResponseV3 = MutableLiveData<ApiResponseX>()

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

    fun deletePost(postId: Long) {
        state.deletePostLoading.value = true

        classCollaborationRepository.deletePost(postId).observeForever {
            state.deletePostLoading.value = false

            val data = it?.data
            data?.let {
                deletePostResponse.value = it
            } ?: {
                deletePostErrorResponse.value = it?.error
            }()
        }
    }

    fun deletePostV3(postId: String) {
        state.deletePostLoading.value = true
        classCollaborationRepository.deletePostV3(postId).observeForever {
            state.deletePostLoading.value = false

            val data = it?.data
            data?.let {
                deletePostResponse.value = it
            } ?: {
                deletePostErrorResponseV3.value = it?.error
            }()
        }
    }

    fun listClassMember(id: Long, isNetworkPreferred: Boolean) {
        state.postLoading.value = true

        classMemberRepository.getClassMembers(id, isNetworkPreferred).observeForever {
            state.postLoading.value = false

            val data = it?.data
            data?.let {
                classMembers.value = it
            } ?: {
                postErrorResponse.value = it?.error
            }()
        }
    }

    fun listPostLikedByUsers(postId: Long) {
        state.postLoading.value = true

        classCollaborationRepository.listPostLikedByUsers(postId).observeForever {
            state.postLoading.value = false

            val data = it?.data
            data?.let {
                postInteractionUsers.value = it
            } ?: {
                postInteractionUsersErrorResponse.value = it?.error
            }()
        }
    }

    fun listPostSeenByUsers(postId: Long) {
        state.postLoading.value = true

        classCollaborationRepository.listPostSeenByUsers(postId).observeForever {
            state.postLoading.value = false

            val data = it?.data
            data?.let {
                postInteractionUsers.value = it
            } ?: {
                postInteractionUsersErrorResponse.value = it?.error
            }()
        }
    }

    fun listPostLikedByUsersV3(postId: String) {
        state.postLoading.value = true

        classCollaborationRepository.listPostLikedByUsersV3(postId).observeForever {
            state.postLoading.value = false

            val data = it?.data
            data?.let {
                postInteractionUsers.value = it
            } ?: {
                postInteractionUsersErrorResponseV3.value = it?.error
            }()
        }
    }

    fun listPostSeenByUsersV3(postId: String) {
        state.postLoading.value = true

        classCollaborationRepository.listPostSeenByUsersV3(postId).observeForever {
            state.postLoading.value = false

            val data = it?.data
            data?.let {
                postInteractionUsers.value = it
            } ?: {
                postInteractionUsersErrorResponseV3.value = it?.error
            }()
        }
    }

    fun getClassMemberV3(classGroupId: String, isNetworkPreferred: Boolean) {
        state.postLoading.value = true
        classMemberRepository.getClassMembersV3(classGroupId, isNetworkPreferred).observeForever {
            state.postLoading.value = false

            val data = it?.data
            data?.let {
                classMembers.value = it
            } ?: {
                classMemberErrorResponseV3.value = it?.error
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

    fun saveOnlineClass(dialog: DialogInterface?, onlineClassPlatformId: String, sectionId: Long, displayName: String?, url: String, isGeneratedUrl: Boolean, isShown: Boolean) {
        state.saveOnlineClassLoading.value = true
        classCollaborationRepository.saveOnlineClass(onlineClassPlatformId, sectionId, displayName, url, isGeneratedUrl, isShown).observeForever {
            state.saveOnlineClassLoading.value = false

            val data = it?.data
            data?.let {
                dialog?.dismiss()
                saveOnlineClassResponse.value = it
            } ?: {
                saveOnlineClassErrorResponse.value = it.error
            }()
        }
    }

    fun listPostsV3(classGroupId: String) {
        state.postLoading.value = true
        classCollaborationRepository.listPostsV3(classGroupId).observeForever {
            state.postLoading.value = false

            val data = it?.data
            data?.let {
                posts.value = it
            } ?: {
                postErrorResponseV3.value = it?.error
            }()
        }
    }

    fun getSharedClassSections(classGroupId: String) {
        classCollaborationRepository.listClassSharedSections(classGroupId).observeForever {
            val data = it.data

            data?.let {
                getSharedClassSectionsResponse.value = it
            } ?: {
                getSharedClassSectionsErrorResponse.value = it.error
            }()
        }
    }

    fun listOnlineClassesV3(classGroupId: String) {
        state.onlineClassesLoading.value = true
        classCollaborationRepository.listOnlineClassesV3(classGroupId).observeForever {
            state.onlineClassesLoading.value = false

            val data = it.data
            data?.let {
                onlineClassesResponse.value = it
            } ?: {
                onlineClassesErrorResponseV3.value = it.error
            }()
        }
    }

    fun saveOnlineClassV3(dialog: DialogInterface?, onlineClassPlatformId: String, classGroupId: String, displayName: String?, url: String, isGeneratedUrl: Boolean, isShown: Boolean) {
        state.saveOnlineClassLoading.value = true
        classCollaborationRepository.saveOnlineClassV3(classGroupId, onlineClassPlatformId, displayName, url, isGeneratedUrl, isShown).observeForever {
            state.saveOnlineClassLoading.value = false

            val data = it.data
            data?.let {
                dialog?.dismiss()
                saveOnlineClassResponse.value = it
            } ?: {
                saveOnlineClassErrorResponseV3.value = it.error
            }()
        }
    }

    fun getMSTeamsUrlV3(classGroupId: String, handleFailedGenerateMSTeamsUrl: () -> Unit) {
        state.getMSTeamsURLLoading.value = true
        classCollaborationRepository.getMSTeamsUrlV3(classGroupId).observeForever {
            state.getMSTeamsURLLoading.value = false

            val data = it.data
            data?.let {
                getMSTeamsURLResponse.value = it

                if (it.onlineClassUrl.isNullOrBlank()) {
                    handleFailedGenerateMSTeamsUrl()
                }
            } ?: {
                getMSTeamsURLErrorResponseV3.value = it.error
                handleFailedGenerateMSTeamsUrl()
            }()
        }
    }

    data class State(
        val postLoading: MutableLiveData<Boolean>,
        val deletePostLoading: MutableLiveData<Boolean>,
        val onlineClassLoading: MutableLiveData<Boolean>,
        val enableOnlineClassLoading: MutableLiveData<Boolean>,
        val onlineClassesLoading: MutableLiveData<Boolean>,
        val getMSTeamsURLLoading: MutableLiveData<Boolean>,
        val saveOnlineClassLoading: MutableLiveData<Boolean>
    )
}