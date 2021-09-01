package whiz.tss.sspark.s_spark_android.data.datasSource.remote.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.AuthenticationInformation
import whiz.sspark.library.data.entity.LoginAPIBody
import whiz.sspark.library.data.entity.RefreshTokenAPIBody

interface LoginService {
    @POST("v2/accounts/login")
    suspend fun getLogin(@Body loginAPIBody: LoginAPIBody): Response<ApiResponseX>

    @POST("v2/accounts/tokens/refresh")
    suspend fun refreshToken(@Body refreshTokenAPIBody: RefreshTokenAPIBody): Response<AuthenticationInformation>
}