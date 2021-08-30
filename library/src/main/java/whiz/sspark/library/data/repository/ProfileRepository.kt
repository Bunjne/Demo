package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.dataSource.remote.service.ProfileService
import whiz.sspark.library.data.entity.DataWrapper
import whiz.sspark.library.data.entity.Student
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetch

interface ProfileRepository {
    suspend fun profile(): Flow<DataWrapper<Student>>
}

class ProfileRepositoryImpl(private val context: Context,
                            private val remote: ProfileService):ProfileRepository {
    override suspend fun profile(): Flow<DataWrapper<Student>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getProfile()
                    fetch(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}