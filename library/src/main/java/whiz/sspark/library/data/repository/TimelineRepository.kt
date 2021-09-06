package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.dataSource.local.impl.TimelineCacheImpl
import whiz.sspark.library.data.dataSource.remote.service.TimelineService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX
import java.util.*

interface TimelineRepository {
    suspend fun getTimeline(date: Date, differDay: Int, isNetworkPreferred: Boolean): Flow<DataWrapperX<TimelineResponse>>
    suspend fun getTodayDate(): Flow<DataWrapperX<TodayDateResponse>>
}

class TimelineRepositoryImpl(private val context: Context,
                             private val local: TimelineCacheImpl,
                             private val remote: TimelineService): TimelineRepository {
    override suspend fun getTimeline(date: Date,
                                     differDay: Int,
                                     isNetworkPreferred: Boolean): Flow<DataWrapperX<TimelineResponse>> {
        return flow {
            val timeline = local.getTimeline(date.convertToDateString(DateTimePattern.serviceDateFormat))
            if (timeline != null && !isNetworkPreferred) {
                emit(DataWrapperX(
                    data = timeline,
                    error = null,
                    isNetworkPreferred = isNetworkPreferred,
                    dataSource = DataSource.CACHE
                ))
            } else {
                if (NetworkManager.isOnline(context)) {
                    try {
                        val response = remote.getTimeline(differDay)
                        fetchX<TimelineResponse>(response)
                    } catch (e: Exception) {
                        throw e
                    }
                } else {
                    throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getTodayDate(): Flow<DataWrapperX<TodayDateResponse>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getTodayDate()
                    fetchX<TodayDateResponse>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}