package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.StudentAdvisoryAppointmentService
import whiz.sspark.library.data.entity.AdvisoryAppointment
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface StudentAdvisoryAppointmentRepository {
    suspend fun getPendingAdvisorySlot(termId: String): Flow<DataWrapperX<List<AdvisoryAppointment>>>
    suspend fun getHistoryAdvisorySlot(termId: String): Flow<DataWrapperX<List<AdvisoryAppointment>>>
    suspend fun reserveAdvisorySlot(slotId: String): Flow<DataWrapperX<String>>
    suspend fun cancelAdvisorySlot(slotId: String): Flow<DataWrapperX<String>>
}

class StudentAdvisoryAppointmentRepositoryImpl(private val context: Context,
                                               private val remote: StudentAdvisoryAppointmentService): StudentAdvisoryAppointmentRepository {
    override suspend fun getPendingAdvisorySlot(termId: String): Flow<DataWrapperX<List<AdvisoryAppointment>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getPendingAdvisorySlot(termId)
                    fetchX(response, Array<AdvisoryAppointment>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getHistoryAdvisorySlot(termId: String): Flow<DataWrapperX<List<AdvisoryAppointment>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getHistoryAdvisorySlot(termId)
                    fetchX(response, Array<AdvisoryAppointment>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun reserveAdvisorySlot(slotId: String): Flow<DataWrapperX<String>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.reserveAdvisorySlot(slotId)
                    fetchX<String>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun cancelAdvisorySlot(slotId: String): Flow<DataWrapperX<String>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.cancelAdvisorySlot(slotId)
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