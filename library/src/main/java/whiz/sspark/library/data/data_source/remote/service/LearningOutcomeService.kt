package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX

interface LearningOutcomeService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/students/me/grades")
    suspend fun getLearningOutcome(@Query("termId") termId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/students/{studentId}/grades")
    suspend fun getLearningOutcome(@Path("studentId") studentId: String,
                                   @Query("termId") termId: String): Response<ApiResponseX>
}