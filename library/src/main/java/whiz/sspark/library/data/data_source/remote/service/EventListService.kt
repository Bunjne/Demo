package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import whiz.sspark.library.data.entity.ApiResponseX

interface EventListService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/events/list")
    suspend fun getEvents(): Response<ApiResponseX>

    @Headers("Content-Type: application/json0")
    @GET("api/v1/events/feature")
    suspend fun getHighlightEvents(): Response<ApiResponseX>
}