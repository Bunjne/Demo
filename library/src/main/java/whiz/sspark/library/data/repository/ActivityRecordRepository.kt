package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.ActivityRecordService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface ActivityRecordRepository {
    suspend fun getActivityRecord(termId: String, studentId: String?): Flow<DataWrapperX<List<ActivityDTO>>>
}

class ActivityRecordRepositoryImpl(private val context: Context,
                                   private val remote: ActivityRecordService): ActivityRecordRepository {
    override suspend fun getActivityRecord(termId: String, studentId: String?): Flow<DataWrapperX<List<ActivityDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = if (studentId.isNullOrBlank()) {
                        remote.getActivityRecord(termId)
                    } else {
                        remote.getActivityRecord(studentId, termId)
                    }

                    fetchX(response, Array<ActivityDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}