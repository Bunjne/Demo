package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.datasource.remote.service.InstructorClassActivityService
import whiz.sspark.library.data.datasource.remote.service.StudentClassActivityService
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.PlatformOnlineClass
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface InstructorClassActivityRepository {
    fun listPosts(classGroupId: String): Flow<DataWrapperX<List<Post>>>
    fun listOnlineClasses(classGroupId: String): Flow<DataWrapperX<List<PlatformOnlineClass>>>
}

class InstructorClassActivityRepositoryImpl(private val context: Context,
                                            private val remote: InstructorClassActivityService): InstructorClassActivityRepository {
    override fun listPosts(classGroupId: String): Flow<DataWrapperX<List<Post>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.listPosts(classGroupId)
                    fetchX(response, Array<Post>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun listOnlineClasses(classGroupId: String): Flow<DataWrapperX<List<PlatformOnlineClass>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.listOnlineClasses(classGroupId)
                    fetchX(response, Array<PlatformOnlineClass>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

}