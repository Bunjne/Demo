package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.LearningOutcomeService
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.LearningOutcomeDTO
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface LearningOutcomeRepository {
    suspend fun getLearningOutcome(termId: String, studentId: String?): Flow<DataWrapperX<List<LearningOutcomeDTO>>>
}

class LearningOutcomeRepositoryImpl(private val context: Context,
                                    private val remote: LearningOutcomeService): LearningOutcomeRepository {
    override suspend fun getLearningOutcome(termId: String, studentId: String?): Flow<DataWrapperX<List<LearningOutcomeDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = if (studentId.isNullOrBlank()) {
                        remote.getLearningOutcome(termId)
                    } else {
                        remote.getLearningOutcome(studentId, termId)
                    }

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