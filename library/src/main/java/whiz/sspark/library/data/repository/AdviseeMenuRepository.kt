package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.AdviseeMenuService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface AdviseeMenuRepository {
    suspend fun getAdviseeInfo(studentId: String): Flow<DataWrapperX<Student>>
    suspend fun getAdviseeMenu(studentId: String): Flow<DataWrapperX<List<MenuDTO>>>
}

class AdviseeMenuRepositoryImpl(private val context: Context,
                                private val remote: AdviseeMenuService): AdviseeMenuRepository {
    override suspend fun getAdviseeInfo(studentId: String): Flow<DataWrapperX<Student>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getAdviseeInfo(studentId)
                    fetchX<Student>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAdviseeMenu(studentId: String): Flow<DataWrapperX<List<MenuDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getAdviseeMenu(studentId)
                    fetchX(response, Array<MenuDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}