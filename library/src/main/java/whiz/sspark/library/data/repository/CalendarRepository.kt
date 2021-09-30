package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.CalendarService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX
import java.util.*

interface CalendarRepository {
    suspend fun getCalendar(termId: String): Flow<DataWrapperX<List<CalendarDTO>>>
    suspend fun getCalendarInfo(): Flow<DataWrapperX<List<CalendarInfoDTO>>>
}

class CalendarRepositoryImpl(private val context: Context,
                             private val remote: CalendarService): CalendarRepository {
    override suspend fun getCalendar(termId: String): Flow<DataWrapperX<List<CalendarDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getCalendar(termId)
                    fetchX(response, Array<CalendarDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getCalendarInfo(): Flow<DataWrapperX<List<CalendarInfoDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getCalendarInfo()
                    fetchX(response, Array<CalendarInfoDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}