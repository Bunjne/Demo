package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.ClassAssignmentService
import whiz.sspark.library.data.entity.AssignmentDTO
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface ClassAssignmentRepository {
    fun getAssignment(classGroupId: String, page: Int, pageSize: Int): Flow<DataWrapperX<AssignmentDTO>>
}

class ClassAssignmentRepositoryImpl(private val context: Context,
                                    private val remote: ClassAssignmentService) : ClassAssignmentRepository {
    override fun getAssignment(classGroupId: String, page: Int, pageSize: Int): Flow<DataWrapperX<AssignmentDTO>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getAssignment(classGroupId, page, pageSize)
                    fetchX<AssignmentDTO>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}