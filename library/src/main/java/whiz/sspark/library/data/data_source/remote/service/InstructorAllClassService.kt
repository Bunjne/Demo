package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface InstructorAllClassService: StudentAllClassService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/instructors/me/schedules/courses")
    override suspend fun getAllClass(@Query("termId") termId: String): Response<ApiResponseX>
}