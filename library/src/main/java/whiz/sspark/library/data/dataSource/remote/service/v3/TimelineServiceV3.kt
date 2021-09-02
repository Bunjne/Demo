package whiz.sspark.library.data.dataSource.remote.service.v3

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.TimelineResponse

interface TimelineServiceV3 {
    @GET("v1/Timelines")
    suspend fun getTimeline(@Query("date") differDay: Int): Response<ApiResponseX>
}