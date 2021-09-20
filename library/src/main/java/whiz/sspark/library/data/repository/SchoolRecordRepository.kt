package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.SchoolRecordService
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface SchoolRecordRepository {
    suspend fun getTerms(): Flow<DataWrapperX<List<Term>>>
}

class SchoolRecordRepositoryImpl(private val context: Context,
                                    private val remote: SchoolRecordService): SchoolRecordRepository {
    override suspend fun getTerms(): Flow<DataWrapperX<List<Term>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getTerms()
                    fetchX(response, Array<Term>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}