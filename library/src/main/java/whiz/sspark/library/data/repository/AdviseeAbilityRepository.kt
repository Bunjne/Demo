package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.AbilityService
import whiz.sspark.library.data.data_source.remote.service.AdviseeAbilityService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface AdviseeAbilityRepository {
    suspend fun getAbility(termId: String, studentId: String): Flow<DataWrapperX<List<AbilityDTO>>>
}

class AdviseeAbilityRepositoryImpl(private val context: Context,
                                   private val remote: AdviseeAbilityService): AdviseeAbilityRepository, AbilityRepositoryImpl(context, remote) {
    override suspend fun getAbility(termId: String, studentId: String): Flow<DataWrapperX<List<AbilityDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getAbility(studentId, termId)
                    fetchX(response, Array<AbilityDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}