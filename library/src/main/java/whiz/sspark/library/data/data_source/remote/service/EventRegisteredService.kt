package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import whiz.sspark.library.data.entity.ApiResponseX

interface EventRegisteredService {
    @GET("api/v1/events/registered")
    suspend fun getRegisteredEvents(): Response<ApiResponseX>
}