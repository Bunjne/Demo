package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface CalendarService {
    @GET("v1/calendar")
    suspend fun getCalendar(@Path(":termId") termId: String): Response<ApiResponseX>

    @GET("v1/calendars/types")
    suspend fun getCalendarInfo(): Response<ApiResponseX>
}