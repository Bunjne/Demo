package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.AdviseeExpectOutcomeService
import whiz.sspark.library.data.data_source.remote.service.ExpectOutcomeService
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.ExpectOutcomeDTO
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface AdviseeExpectOutcomeRepository {
    suspend fun getExpectOutcome(courseId: String, termId: String, studentId: String): Flow<DataWrapperX<ExpectOutcomeDTO>>
}

class AdviseeExpectOutcomeRepositoryImpl(private val context: Context,
                                         private val remote: AdviseeExpectOutcomeService):AdviseeExpectOutcomeRepository, ExpectOutcomeRepositoryImpl(context, remote) {

    override suspend fun getExpectOutcome(courseId: String, termId: String, studentId: String): Flow<DataWrapperX<ExpectOutcomeDTO>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getExpectOutcome(studentId, courseId, termId)
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