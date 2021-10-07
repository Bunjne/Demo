package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface TimelineService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/date/today")
    suspend fun getTodayDate(): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/Timelines")
    suspend fun getTimeline(@Query("date") differDay: Int): Response<ApiResponseX>
}