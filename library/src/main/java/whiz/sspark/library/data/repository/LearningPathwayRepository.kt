package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.LearningPathwayService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface LearningPathwayRepository {
    suspend fun getLearningPathway(): Flow<DataWrapperX<List<LearningPathwayDTO>>>
    suspend fun addCourse(termId: String, courseId: String): Flow<DataWrapperX<String>>
    suspend fun deleteCourse(termId: String, courseId: String): Flow<DataWrapperX<String>>
}

class LearningPathwayRepositoryImpl(private val context: Context,
                                    private val remote: LearningPathwayService): LearningPathwayRepository {
    override suspend fun getLearningPathway(): Flow<DataWrapperX<List<LearningPathwayDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getLearningPathway()
                    fetchX(response, Array<LearningPathwayDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addCourse(termId: String, courseId: String): Flow<DataWrapperX<String>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.addCourse(LearningPathwayAddCourseAPIBody(termId, courseId))
                    fetchX<String>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteCourse(termId: String, courseId: String): Flow<DataWrapperX<String>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.deleteCourse(termId, courseId)
                    fetchX<String>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}