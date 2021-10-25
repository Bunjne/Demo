package whiz.tss.sspark.s_spark_android.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.AuthenticationInformation
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.utility.NetworkManager
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.data.dataSource.remote.service.LoginService
import whiz.tss.sspark.s_spark_android.data.enum.GrantType

interface LoginRepository {
    suspend fun login(username: String, password: String): Flow<DataWrapperX<AuthenticationInformation>>
    suspend fun refreshToken(refreshToken: String): Flow<DataWrapperX<AuthenticationInformation>>
}

class LoginRepositoryImpl(private val context: Context,
                          private val remote: LoginService): LoginRepository {
    override suspend fun login(username: String, password: String): Flow<DataWrapperX<AuthenticationInformation>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getLogin(
                        client_id = SSparkLibrary.clientId,
                        client_secret = SSparkLibrary.clientSecret,
                        username = username,
                        password = password,
                        grant_type = GrantType.LOGIN.type
                    )

                    emit(DataWrapperX(
                        data = response.body(),
                        error = ApiResponseX(statusCode = response.code()),
                        dataSource = DataSource.NETWORK
                    ))
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun refreshToken(refreshToken: String): Flow<DataWrapperX<AuthenticationInformation>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.refreshToken(
                        client_id = SSparkLibrary.clientId,
                        client_secret = SSparkLibrary.clientSecret,
                        refreshToken = refreshToken,
                        grant_type = GrantType.REFRESH_TOKEN.type
                    )

                    when {
                        response.isSuccessful -> {
                            emit(DataWrapperX(
                                data = response.body(),
                                error = ApiResponseX(statusCode = response.code()),
                                dataSource = DataSource.NETWORK
                            ))
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