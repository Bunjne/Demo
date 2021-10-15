package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.local.impl.ClassMemberCacheImpl
import whiz.sspark.library.data.data_source.remote.service.ClassPostCommentService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface ClassPostCommentRepository {
    fun listComments(postId: String): Flow<DataWrapperX<List<Comment>>>
    fun listClassMembers(classGroupId: String, isNetworkPreferred: Boolean): Flow<DataWrapperX<Member>>
    fun addComment(postId: String, message: String): Flow<DataWrapperX<String>>
    fun deleteComment(postId: String, commentId: String): Flow<DataWrapperX<String>>
    fun deletePost(postId: String): Flow<DataWrapperX<String>> //TODO wait for Response confirmation from API team
}

class ClassPostCommentRepositoryImpl(private val context: Context,
                                     private val local: ClassMemberCacheImpl,
                                     private val remote: ClassPostCommentService): ClassPostCommentRepository {
    override fun listComments(postId: String): Flow<DataWrapperX<List<Comment>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.listComments(postId)
                    fetchX(response, Array<Comment>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun listClassMembers(classGroupId: String,
                                  isNetworkPreferred: Boolean): Flow<DataWrapperX<Member>> {
        return flow {
            val localClassMembers = local.getClassMembers(classGroupId)
            if (localClassMembers != null && !isNetworkPreferred) {
                emit(DataWrapperX(
                    data = localClassMembers,
                    error = null,
                    statusCode = null,
                    isCacheExisted = true,
                    latestDateTime = local.getClassMembersLatestDateTime(),
                    isNetworkPreferred = isNetworkPreferred,
                    dataSource = DataSource.CACHE
                ))
            } else {
                if (NetworkManager.isOnline(context)) {
                    try {
                        val response = remote.listClassMembers(classGroupId)
                        fetchX<Member>(response)
                    } catch (e: Exception) {
                        throw e
                    }
                } else {
                    throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun addComment(postId: String, message: String): Flow<DataWrapperX<String>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.addComment(postId, AddCommentAPIBody(message))
                    fetchX<String>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun deleteComment(postId: String, commentId: String): Flow<DataWrapperX<String>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.deleteComment(postId, commentId)
                    fetchX<String>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun deletePost(postId: String): Flow<DataWrapperX<String>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.deletePost(postId)
                    fetchX<String>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

}