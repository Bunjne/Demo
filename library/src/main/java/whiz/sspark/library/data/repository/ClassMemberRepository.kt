package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.local.impl.ClassMemberCacheImpl
import whiz.sspark.library.data.data_source.remote.service.ClassMemberService
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.Member
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface ClassMemberRepository {
    fun getClassMember(classId: String, isNetworkPreferred: Boolean): Flow<DataWrapperX<Member>>
}

class ClassMemberRepositoryImpl(private val context: Context,
                                private val local: ClassMemberCacheImpl,
                                private val remote: ClassMemberService): ClassMemberRepository {
    override fun getClassMember(classId: String, isNetworkPreferred: Boolean): Flow<DataWrapperX<Member>> {
        return flow {
            val localMember = local.getClassMembers(classId)
            if (localMember != null && !isNetworkPreferred) {
                emit(DataWrapperX(
                    data = localMember,
                    error = null,
                    statusCode = null,
                    isCacheExisted = true,
                    latestDateTime = local.getClassMembersLatestDateTime(),
                    isNetworkPreferred = isNetworkPreferred,
                    dataSource = DataSource.CACHE
                ))
            } else {
                if (NetworkManager.isOnline(context)) {
                    try {
                        val response = remote.getClassMember(classId)
                        val member = response.body()?.data?.toObject<Member>()

                        member?.let {
                            local.saveClassMembers(classId, member)
                        }

                        fetchX<Member>(response)
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