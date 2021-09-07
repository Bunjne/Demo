package whiz.sspark.library.data.datasource.remote.service

import retrofit2.Response
import retrofit2.http.GET
import whiz.sspark.library.data.entity.ApiResponseX

interface MenuService {
    @GET("v1/menu")
    suspend fun getMenu(): Response<ApiResponseX>

    @GET("v1/menu/grade_summary")
    suspend fun getGradeSummary(): Response<ApiResponseX>

    @GET("v1/menu/calendar")
    suspend fun getCalendar(): Response<ApiResponseX>

    @GET("v1/menu/notification_inbox")
    suspend fun getNotificationInbox(): Response<ApiResponseX>

    @GET("v1/menu/advising_note")
    suspend fun getAdvisingNote(): Response<ApiResponseX>
}