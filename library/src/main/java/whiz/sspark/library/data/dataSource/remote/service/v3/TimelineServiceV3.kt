package whiz.sspark.library.data.dataSource.remote.service.v3

import retrofit2.Response
import retrofit2.http.GET
import whiz.sspark.library.data.entity.TimelineResponse

interface TimelineServiceV3 {
    @GET("v3/Timelines")
    suspend fun getTimeline(differDay: Int): Response<TimelineResponse>
}