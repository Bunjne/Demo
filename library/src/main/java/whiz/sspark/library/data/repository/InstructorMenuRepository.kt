package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.InstructorMenuService
import whiz.sspark.library.data.data_source.remote.service.StudentMenuService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface InstructorMenuRepository {
    suspend fun getMenu(): Flow<DataWrapperX<List<MenuDTO>>>
    suspend fun getCalendar(): Flow<DataWrapperX<MenuCalendarDTO>>
    suspend fun getNotificationInbox(): Flow<DataWrapperX<MenuNotificationInboxDTO>>
}

class InstructorMenuRepositoryImpl(private val context: Context,
                                   private val remote: InstructorMenuService): InstructorMenuRepository {

    override suspend fun getMenu(): Flow<DataWrapperX<List<MenuDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getMenu()
                    fetchX(response, Array<MenuDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getCalendar(): Flow<DataWrapperX<MenuCalendarDTO>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getCalendar()
                    fetchX<MenuCalendarDTO>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getNotificationInbox(): Flow<DataWrapperX<MenuNotificationInboxDTO>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getNotificationInbox()
                    fetchX<MenuNotificationInboxDTO>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}