package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.ClassGroupService
import whiz.sspark.library.data.entity.ClassGroup
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface ClassGroupRepository {
    fun getClassGroup(): Flow<DataWrapperX<ClassGroup>>
}

class ClassGroupRepositoryImpl(private val context: Context,
                               private val remote: ClassGroupService): ClassGroupRepository {
    override fun getClassGroup(): Flow<DataWrapperX<ClassGroup>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getClassGroup()

                    fetchX<ClassGroup>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}