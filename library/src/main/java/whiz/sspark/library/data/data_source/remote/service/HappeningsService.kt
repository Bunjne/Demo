package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface HappeningsService {
    @Headers("Content-Type: application/json")
    @GET("api/v3/news/today")
    suspend fun getTodayNews(): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v3/events")
    suspend fun getEvents(@Query("type") type: String): Response<ApiResponseX>
}