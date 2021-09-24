package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface LearningOutcomeService {
    @GET("v1/students/me/grades")
    suspend fun getLearningOutcome(@Query("termId") termId: String): Response<ApiResponseX>
}