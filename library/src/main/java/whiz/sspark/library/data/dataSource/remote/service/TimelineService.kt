package whiz.sspark.library.data.dataSource.remote.service

import retrofit2.Response
import retrofit2.http.GET
import whiz.sspark.library.data.entity.ApiResponseX

interface TimelineService {
    @GET("v1/todays")
    suspend fun getTodayDate(): Response<ApiResponseX>
}