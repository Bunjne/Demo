package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.LikeBySeenByService
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.Member
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX
import java.lang.Exception

interface LikeBySeenByRepository {
    suspend fun getMembersByPostLiked(postId: String): Flow<DataWrapperX<Member>>
    suspend fun getMembersByPostSeen(postId: String): Flow<DataWrapperX<Member>>
}

class LikeBySeenByRepositoryImpl(private val context: Context,
                                 private val remote: LikeBySeenByService): LikeBySeenByRepository {

    override suspend fun getMembersByPostLiked(postId: String): Flow<DataWrapperX<Member>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getMembersByPostLiked(postId)
                    fetchX<Member>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getMembersByPostSeen(postId: String): Flow<DataWrapperX<Member>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getMembersByPostSeen(postId)
                    fetchX<Member>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }
    }
}