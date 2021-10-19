package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.CourseSyllabusService
import whiz.sspark.library.data.entity.CourseSyllabusDTO
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface CourseSyllabusRepository {
    suspend fun getCourseDetail(classGroupId: String, termId: String): Flow<DataWrapperX<CourseSyllabusDTO>>
}

class CourseSyllabusRepositoryImpl(private val context: Context,
                                   private val remote: CourseSyllabusService): CourseSyllabusRepository {

    override suspend fun getCourseDetail(classGroupId: String, termId: String): Flow<DataWrapperX<CourseSyllabusDTO>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getCourseDetail(classGroupId, termId)
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