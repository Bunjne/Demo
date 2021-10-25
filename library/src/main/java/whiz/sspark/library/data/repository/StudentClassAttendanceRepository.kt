package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.StudentClassAttendanceService
import whiz.sspark.library.data.entity.Attendance
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface StudentAttendanceRepository {
    fun getClassAttendance(classId: String): Flow<DataWrapperX<Attendance>>
}

class StudentClassAttendanceRepositoryImpl(private val context: Context,
                                           private val remote: StudentClassAttendanceService) : StudentAttendanceRepository {
    override fun getClassAttendance(classGroupId: String): Flow<DataWrapperX<Attendance>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getClassAttendance(classGroupId)
                    fetchX<Attendance>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}