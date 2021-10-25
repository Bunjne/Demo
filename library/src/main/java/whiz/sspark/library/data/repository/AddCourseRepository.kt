package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.AddCourseService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface AddCourseRepository {
    suspend fun getConcentrateCourse(): Flow<DataWrapperX<List<ConcentrateCourseDTO>>>
}

class AddCourseRepositoryImpl(private val context: Context,
                              private val remote: AddCourseService): AddCourseRepository {
    override suspend fun getConcentrateCourse(): Flow<DataWrapperX<List<ConcentrateCourseDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getConcentrateCourse()
                    fetchX(response, Array<ConcentrateCourseDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}