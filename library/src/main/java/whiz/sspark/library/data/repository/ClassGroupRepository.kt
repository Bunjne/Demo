package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.local.impl.ClassGroupCacheImpl
import whiz.sspark.library.data.data_source.remote.service.ClassGroupService
import whiz.sspark.library.data.entity.ClassGroup
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface ClassGroupRepository {
    fun getClassGroup(isNetworkPreferred: Boolean): Flow<DataWrapperX<List<ClassGroup>>>
}

class ClassGroupRepositoryImpl(private val context: Context,
                               private val local: ClassGroupCacheImpl,
                               private val remote: ClassGroupService): ClassGroupRepository {
    override fun getClassGroup(isNetworkPreferred: Boolean): Flow<DataWrapperX<List<ClassGroup>>> {
        return flow {
            val localClassGroup = local.getClassGroups()
            if (localClassGroup != null && !isNetworkPreferred) {
                emit(DataWrapperX(
                    data = localClassGroup,
                    error = null,
                    statusCode = null,
                    isCacheExisted = true,
                    latestDateTime = local.getClassGroupLatestDateTime(),
                    dataSource = DataSource.CACHE
                ))
            } else {
                if (NetworkManager.isOnline(context)) {
                    try {
                        val response = remote.getClassGroup()
                        val saveClassGroup = response.body()?.data?.toObjects(Array<ClassGroup>::class.java)

                        saveClassGroup?.let {
                            local.saveClassGroups(saveClassGroup)
                        }

                        fetchX(response, Array<ClassGroup>::class.java)
                    } catch (e: Exception) {
                        throw e
                    }
                } else {
                    throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}