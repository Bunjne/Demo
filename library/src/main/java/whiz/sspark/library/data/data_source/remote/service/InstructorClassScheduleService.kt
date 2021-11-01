package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface InstructorClassScheduleService: StudentClassScheduleService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/instructors/me/schedules")
    override suspend fun getClassSchedule(@Query("termId") termId: String,
                                          @Query("fromDate") fromDate: String,
                                          @Query("toDate") toDate: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/instructors/me/terms")
    override suspend fun getTerms(): Response<ApiResponseX>
}