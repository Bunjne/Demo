package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.dataSource.local.impl.TimelineCacheImpl
import whiz.sspark.library.data.dataSource.remote.service.TimelineService
import whiz.sspark.library.data.dataSource.remote.service.v3.TimelineServiceV3
import whiz.sspark.library.data.entity.DataWrapper
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.TimelineResponse
import whiz.sspark.library.data.entity.TodayDateResponse
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.extension.toNormalDate
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetch
import whiz.sspark.library.utility.fetchX
import java.util.*

interface TimelineRepository {
    suspend fun getTimeline(date: Date, differDay: Int, isNetworkPreferred: Boolean): Flow<DataWrapperX<TimelineResponse>>
    suspend fun getTodayDate(): Flow<DataWrapper<TodayDateResponse>>
}

class TimelineRepositoryImpl(private val context: Context,
                             private val local: TimelineCacheImpl,
                             private val remote: TimelineService,
                             private val remoteV3: TimelineServiceV3): TimelineRepository {
    override suspend fun getTimeline(date: Date,
                                     differDay: Int,
                                     isNetworkPreferred: Boolean): Flow<DataWrapperX<TimelineResponse>> {
        return flow {
            val timeline = local.getTimeline(date.toNormalDate())
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
                        val response = remoteV3.getTimeline(differDay)
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

    override suspend fun getTodayDate(): Flow<DataWrapper<TodayDateResponse>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getTodayDate()
                    fetch(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}