package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.CourseSyllabusDetailService
import whiz.sspark.library.data.data_source.remote.service.CourseSyllabusWeekDetailService
import whiz.sspark.library.data.data_source.remote.service.ExpectOutcomeService
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.ExpectOutcomeDTO
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface CourseSyllabusWeekDetailRepository {
    suspend fun getCourseWeekDetail(classGroupId: String): Flow<DataWrapperX<ExpectOutcomeDTO>> //TODO wait confirm object
}

class CourseSyllabusWeekDetailRepositoryImpl(private val context: Context,
                                         private val remote: CourseSyllabusWeekDetailService): CourseSyllabusWeekDetailRepository {

    override suspend fun getCourseWeekDetail(classGroupId: String): Flow<DataWrapperX<ExpectOutcomeDTO>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getCourseWeekDetail(classGroupId)
                    fetchX<ExpectOutcomeDTO>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}