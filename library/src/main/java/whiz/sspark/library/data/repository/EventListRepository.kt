package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.EventListService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface EventListRepository {
    suspend fun getEvents(): Flow<DataWrapperX<List<EventListDTO>>>
    suspend fun getHighlightEvents(): Flow<DataWrapperX<List<EventListDTO>>>
}

class EventListRepositoryImpl(private val context: Context,
                              private val remote: EventListService): EventListRepository {
    override suspend fun getEvents(): Flow<DataWrapperX<List<EventListDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getEvents()
                    fetchX(response, Array<EventListDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getHighlightEvents(): Flow<DataWrapperX<List<EventListDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getHighlightEvents()
                    fetchX(response, Array<EventListDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}