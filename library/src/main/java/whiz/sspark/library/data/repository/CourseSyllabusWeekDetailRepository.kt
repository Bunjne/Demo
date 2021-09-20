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
import whiz.sspark.library.data.entity.CourseSyllabusDTO
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.ExpectOutcomeDTO
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface CourseSyllabusWeekDetailRepository {
    suspend fun getCourseWeekDetail(classGroupId: String, termId: String): Flow<DataWrapperX<CourseSyllabusDTO>>
}

class CourseSyllabusWeekDetailRepositoryImpl(private val context: Context,
                                         private val remote: CourseSyllabusWeekDetailService): CourseSyllabusWeekDetailRepository {

    override suspend fun getCourseWeekDetail(classGroupId: String, termId: String): Flow<DataWrapperX<CourseSyllabusDTO>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getCourseWeekDetail(classGroupId, termId)
                    fetchX<CourseSyllabusDTO>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}