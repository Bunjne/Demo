package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.datasource.remote.service.LearningOutcomeService
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.LearningOutcomeAPIBody
import whiz.sspark.library.data.entity.LearningOutcomeDTO
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface LearningOutcomeRepository {
    suspend fun getLearningOutcome(currentSemesterId: Int): Flow<DataWrapperX<List<LearningOutcomeDTO>>>
}

class LearningOutcomeRepositoryImpl(private val context: Context,
                                    private val remote: LearningOutcomeService): LearningOutcomeRepository {
    override suspend fun getLearningOutcome(currentSemesterId: Int): Flow<DataWrapperX<List<LearningOutcomeDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getLearningOutcome(LearningOutcomeAPIBody(currentSemesterId))
                    fetchX(response, Array<LearningOutcomeDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}