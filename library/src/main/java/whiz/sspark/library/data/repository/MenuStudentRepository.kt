package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.MenuStudentService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.NetworkManager
import java.io.BufferedReader
import java.io.InputStreamReader
import whiz.sspark.library.utility.fetchX

interface MenuStudentRepository {
    suspend fun getMenu(): Flow<DataWrapperX<List<MenuDTO>>>
    suspend fun getGradeSummary(): Flow<DataWrapperX<List<MenuGradeSummaryDTO>>>
    suspend fun getCalendar(): Flow<DataWrapperX<MenuCalendarDTO>>
    suspend fun getNotificationInbox(): Flow<DataWrapperX<MenuNotificationInboxDTO>>
    suspend fun getAdvisingNote(): Flow<DataWrapperX<MenuAdvisingNoteDTO>>
}

class MenuStudentRepositoryImpl(private val context: Context,
                                private val remote: MenuStudentService):MenuStudentRepository {

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

    override suspend fun getGradeSummary(): Flow<DataWrapperX<List<MenuGradeSummaryDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getGradeSummary()
                    fetchX(response, Array<MenuGradeSummaryDTO>::class.java)
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

    override suspend fun getAdvisingNote(): Flow<DataWrapperX<MenuAdvisingNoteDTO>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getAdvisingNote()
                    fetchX<MenuAdvisingNoteDTO>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}