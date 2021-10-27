package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.StudentAllClassService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface StudentAllClassesRepository {
    fun getAllClass(termId: String): Flow<DataWrapperX<List<ClassScheduleAllClassDTO>>>
}

open class StudentAllClassRepositoryImpl(private val context: Context,
                                         private val remote: StudentAllClassService): StudentAllClassesRepository {
    override fun getAllClass(termId: String): Flow<DataWrapperX<List<ClassScheduleAllClassDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getAllClass(termId)
                    fetchX(response, Array<ClassScheduleAllClassDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}