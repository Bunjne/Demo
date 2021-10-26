package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface AdviseeClassScheduleService: StudentClassScheduleService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/students/{studentId}/terms")
    suspend fun getTerms(@Path("studentId") studentId: String): Response<ApiResponseX>
}