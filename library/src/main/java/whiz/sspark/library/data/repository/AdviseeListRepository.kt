package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.AbilityService
import whiz.sspark.library.data.data_source.remote.service.AdviseeListService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface AdviseeListRepository {
    suspend fun getAdviseeList(): Flow<DataWrapperX<AdviseeListDTO>>
}

class AdviseeListRepositoryImpl(private val context: Context,
                                private val remote: AdviseeListService): AdviseeListRepository {
    override suspend fun getAdviseeList(): Flow<DataWrapperX<AdviseeListDTO>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getAdviseeList()
                    fetchX<AdviseeListDTO>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}