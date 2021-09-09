package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import whiz.sspark.library.data.entity.ApiResponseX

interface MenuStudentService {
    @GET("v1/menus") //TODO wait confirm path
    suspend fun getMenu(): Response<ApiResponseX>

    @GET("v1/menu/grade_summary") //TODO wait confirm path
    suspend fun getGradeSummary(): Response<ApiResponseX>

    @GET("v1/menu/calendar") //TODO wait confirm path
    suspend fun getCalendar(): Response<ApiResponseX>

    @GET("v1/menu/notification_inbox") //TODO wait confirm path
    suspend fun getNotificationInbox(): Response<ApiResponseX>

    @GET("v1/menu/advising_note") //TODO wait confirm path
    suspend fun getAdvisingNote(): Response<ApiResponseX>
}