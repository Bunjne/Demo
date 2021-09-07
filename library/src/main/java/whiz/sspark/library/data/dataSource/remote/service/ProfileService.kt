package whiz.sspark.library.data.dataSource.remote.service

import retrofit2.Response
import retrofit2.http.GET
import whiz.sspark.library.data.entity.ApiResponseX

interface ProfileService {
    @GET("v1/students/me")
    suspend fun getProfile(): Response<ApiResponseX>
}