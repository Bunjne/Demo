package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.NotificationInboxService
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.InboxDTO
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface NotificationInboxRepository {
    suspend fun getNotification(page: Int, pageSize: Int): Flow<DataWrapperX<InboxDTO>>
}

class NotificationInboxRepositoryImpl(private val context: Context,
                                      private val remote: NotificationInboxService): NotificationInboxRepository {
    override suspend fun getNotification(page: Int, pageSize: Int): Flow<DataWrapperX<InboxDTO>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getNotification(page, pageSize)
                    fetchX<InboxDTO>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}