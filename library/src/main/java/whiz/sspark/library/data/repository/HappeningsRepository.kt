package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.dataSource.local.impl.HappeningsCacheImpl
import whiz.sspark.library.data.dataSource.remote.service.HappeningsService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface HappeningsRepository {
    suspend fun getTodayNews(): Flow<DataWrapperX<List<News>>>
    suspend fun getEvents(type: String, isNetworkPreferred: Boolean): Flow<DataWrapperX<List<Event>>>
}

class HappeningsRepositoryImpl(private val context: Context,
                               private val local: HappeningsCacheImpl,
                               private val remote: HappeningsService
): HappeningsRepository {
    override suspend fun getTodayNews(): Flow<DataWrapperX<List<News>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getTodayNews()
                    fetchX<List<News>>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getEvents(type: String, isNetworkPreferred: Boolean): Flow<DataWrapperX<List<Event>>> {
        return flow {
            val localEvents = local.listEvents()
            if (localEvents != null && !isNetworkPreferred) {
                emit(DataWrapperX(
                    data = localEvents,
                    error = null,
                    statusCode = null,
                    isCacheExisted = true,
                    latestDateTime = local.getEventListLatestDateTime(),
                    isNetworkPreferred = isNetworkPreferred,
                    dataSource = DataSource.CACHE
                ))
            } else {
                if (NetworkManager.isOnline(context)) {
                    try {
                        val response = remote.getEvents(type)
                        fetchX<List<Event>>(response)
                    } catch (e: Exception) {
                        throw e
                    }
                } else {
                    throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

}