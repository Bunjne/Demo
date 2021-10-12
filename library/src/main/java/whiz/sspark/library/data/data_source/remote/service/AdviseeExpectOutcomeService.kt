package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface AdviseeExpectOutcomeService: ExpectOutcomeService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/students/{studentId}/grades/courses/{courseId}")
    suspend fun getExpectOutcome(@Path("studentId") studentId: String,
                                 @Path("courseId") courseId: String,
                                 @Query("termId") termId: String): Response<ApiResponseX>
}