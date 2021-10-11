package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import whiz.sspark.library.data.entity.ApiResponseX

interface InstructorMenuService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/menus")
    suspend fun getMenu(): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/calendars/today")
    suspend fun getCalendar(): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/notification/widget")
    suspend fun getNotificationInbox(): Response<ApiResponseX>
}