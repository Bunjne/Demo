package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.ClassGroupService
import whiz.sspark.library.data.data_source.remote.service.ClassScheduleService
import whiz.sspark.library.data.entity.ClassGroup
import whiz.sspark.library.data.entity.ClassScheduleDTO
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface ClassScheduleRepository {
    fun getClassSchedule(termId: String, fromDate: String, toDate: String): Flow<DataWrapperX<List<ClassScheduleDTO>>>
}

class ClassScheduleRepositoryImpl(private val context: Context,
                               private val remote: ClassScheduleService): ClassScheduleRepository {
    override fun getClassSchedule(termId: String, fromDate: String, toDate: String): Flow<DataWrapperX<List<ClassScheduleDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getClassSchedule(termId, fromDate, toDate)
                    fetchX(response, Array<ClassScheduleDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}