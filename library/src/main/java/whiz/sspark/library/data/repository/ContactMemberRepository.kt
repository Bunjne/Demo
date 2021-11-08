package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.ContactMemberService
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.StudentInstructorDTO
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX
import java.util.*

interface ContactMemberRepository {
    suspend fun getContactMembers(contactGroupId: String): Flow<DataWrapperX<List<StudentInstructorDTO>>>
}

class ContactMemberRepositoryImpl(private val context: Context,
                                  private val remote: ContactMemberService) : ContactMemberRepository {
    override suspend fun getContactMembers(contactGroupId: String): Flow<DataWrapperX<List<StudentInstructorDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getContactMembers(contactGroupId)
                    fetchX(response, Array<StudentInstructorDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}