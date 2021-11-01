package whiz.tss.sspark.s_spark_android.data.dataSource.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.AuthenticationInformation

interface LoginService {
    @FormUrlEncoded
    @POST("connect/token")
    suspend fun getLogin(@Field("client_id") client_id: String,
                         @Field("client_secret") client_secret: String,
                         @Field("username") username: String,
                         @Field("password") password: String,
                         @Field("grant_type") grant_type: String): Response<AuthenticationInformation>

    @FormUrlEncoded
    @POST("connect/token")
    suspend fun refreshToken(@Field("client_id") client_id: String,
                             @Field("client_secret") client_secret: String,
                             @Field("refreshToken") refreshToken: String,
                             @Field("grant_type") grant_type: String): Response<AuthenticationInformation>
}