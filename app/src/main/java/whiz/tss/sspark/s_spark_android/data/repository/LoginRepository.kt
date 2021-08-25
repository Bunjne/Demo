package whiz.tss.sspark.s_spark_android.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.data.entity.AuthenticationInformation
import whiz.tss.sspark.s_spark_android.data.datasSource.remote.service.LoginService
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.LoginAPIBody
import whiz.sspark.library.data.entity.RefreshTokenAPIBody
import whiz.sspark.library.data.enum.DATASOURCE
import whiz.sspark.library.utility.NetworkManager
import java.lang.Exception

interface LoginRepository {
    suspend fun login(username: String, password: String, uuid: String,): Flow<DataWrapperX<AuthenticationInformation>>
    suspend fun refreshToken(userId: String, uuid: String, refreshToken: String): Flow<AuthenticationInformation>
}

class LoginRepositoryImpl(private val context: Context,
                          private val remote: LoginService): LoginRepository {
    override suspend fun login(username: String, password: String, uuid: String): Flow<DataWrapperX<AuthenticationInformation>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getLogin(LoginAPIBody(username, password, uuid, "Android"))
                    when {
                        response.isSuccessful -> {
                            response.body()?.let {
                                emit(DataWrapperX(
                                    data = it,
                                    error = null,
                                    dataSource = DATASOURCE.NETWORK
                                ))
                            }
                        }
                        else -> throw Exception(response.toString())
                    }
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception("No internet")
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
                throw Exception("No internet")
            }
        }.flowOn(Dispatchers.IO)
    }
}