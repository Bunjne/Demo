package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.AdviseeLearningPathwayService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface AdviseeLearningPathwayRepository {
    suspend fun getLearningPathway(studentId: String): Flow<DataWrapperX<List<LearningPathwayDTO>>>
}

class AdviseeLearningPathwayRepositoryImpl(private val context: Context,
                                           private val remote: AdviseeLearningPathwayService): AdviseeLearningPathwayRepository, LearningPathwayRepositoryImpl(context, remote) {
    override suspend fun getLearningPathway(studentId: String): Flow<DataWrapperX<List<LearningPathwayDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getLearningPathway(studentId)
                    fetchX(response, Array<LearningPathwayDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}