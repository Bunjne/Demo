package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.ContactMemberService
import whiz.sspark.library.data.data_source.remote.service.ContactService
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.StudentInstructorDTO
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX
import java.util.*

interface ContactRepository {
    suspend fun getContacts(): Flow<DataWrapperX<List<Contact>>>
}

class ContactRepositoryImpl(private val context: Context,
                            private val remote: ContactService) : ContactRepository {
    override suspend fun getContacts(): Flow<DataWrapperX<List<Contact>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getContacts()
                    fetchX(response, Array<Contact>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}