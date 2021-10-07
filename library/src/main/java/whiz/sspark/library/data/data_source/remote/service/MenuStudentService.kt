package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface MenuStudentService {
    @Headers("Content-Type: application/json", "x-mock-match-request-headers: Authorization")
    @GET("api/v1/menus")
    suspend fun getMenu(): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/students/me/grades")
    suspend fun getGradeSummary(@Query("termId") termId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/menu/calendar") //TODO wait confirm path
    suspend fun getCalendar(): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/menu/notification_inbox") //TODO wait confirm path
    suspend fun getNotificationInbox(): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/menu/advising_note") //TODO wait confirm path
    suspend fun getAdvisingNote(): Response<ApiResponseX>
}