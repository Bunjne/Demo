package whiz.tss.sspark.s_spark_android.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.data.entity.AuthenticationInformation
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.LoginAPIBody
import whiz.sspark.library.data.entity.RefreshTokenAPIBody
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.transformToDataWrapperX
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.data.dataSource.remote.service.LoginService

interface LoginRepository {
    suspend fun login(username: String, password: String, uuid: String, operator: String): Flow<DataWrapperX<AuthenticationInformation>>
    suspend fun refreshToken(userId: String, uuid: String, refreshToken: String): Flow<AuthenticationInformation>
}

class LoginRepositoryImpl(private val context: Context,
                          private val remote: LoginService): LoginRepository {
    override suspend fun login(username: String, password: String, uuid: String, operator: String): Flow<DataWrapperX<AuthenticationInformation>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getLogin(LoginAPIBody(username, password, uuid, operator))

                    val dataWrapperX = transformToDataWrapperX<AuthenticationInformation>(response)
                    emit(dataWrapperX)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun refreshToken(userId: String, uuid: String, refreshToken: String): Flow<AuthenticationInformation> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.refreshToken(RefreshTokenAPIBody(userId, uuid, refreshToken))
                    when {
                        response.isSuccessful -> {
                            response.body()?.let {
                                emit(it)
                            }
                        }
                        else -> throw Exception(response.toString())
                    }
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}