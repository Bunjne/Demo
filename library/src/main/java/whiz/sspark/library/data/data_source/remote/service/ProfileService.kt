package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import whiz.sspark.library.data.entity.ApiResponseX

interface ProfileService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/students/me")
    suspend fun getStudentProfile(): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/instructors/me")
    suspend fun getInstructorProfile(): Response<ApiResponseX>
}