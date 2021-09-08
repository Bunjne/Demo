package whiz.sspark.library.data.datasource.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface TimelineService {
    @GET("v1/todays")
    suspend fun getTodayDate(): Response<ApiResponseX>

    @GET("v1/Timelines")
    suspend fun getTimeline(@Query("date") differDay: Int): Response<ApiResponseX>
}