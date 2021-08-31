package whiz.sspark.library.data.dataSource.remote.service.v3

import retrofit2.Response
import retrofit2.http.GET
import whiz.sspark.library.data.entity.Event
import whiz.sspark.library.data.entity.News

interface HappeningsServiceV3 {
    @GET("v3/news/today")
    suspend fun getTodayNews(): Response<List<News>>

    @GET("v3/events")
    suspend fun getEvents(): Response<List<Event>>
}