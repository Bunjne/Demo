package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.local.impl.ContactCacheImpl
import whiz.sspark.library.data.dataSource.remote.service.ContactService
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.data.entity.DataWrapper
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetch
import whiz.sspark.library.utility.fetchX

interface ContactRepository {
    suspend fun getContacts(isNetworkPreferred: Boolean): Flow<DataWrapper<List<Contact>>>
}
class ContactRepositoryImpl(private val context: Context,
                            private val local: ContactCacheImpl,
                            private val remote: ContactService): ContactRepository {
    override suspend fun getContacts(isNetworkPreferred: Boolean): Flow<DataWrapper<List<Contact>>> {
        return flow {
            val localContacts = local.getContacts()
            if (localContacts != null && !isNetworkPreferred) {
                emit(
                    DataWrapper(
                    data = localContacts,
                    error = null,
                    statusCode = null,
                    isCacheExisted = true,
                    latestDateTime = local.getLatestDateTime(),
                    isNetworkPreferred = isNetworkPreferred,
                    dataSource = DataSource.CACHE
                )
                )
            } else {
                if (NetworkManager.isOnline(context)) {
                    try {
                        val response = remote.getContacts()
                        fetch(response)
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