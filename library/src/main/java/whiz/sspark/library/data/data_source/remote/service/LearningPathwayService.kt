package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.LearningPathwayCourseAPIBody

interface LearningPathwayService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/pathways/me")
    suspend fun getLearningPathway(): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @POST("api/v1/pathways")
    suspend fun addCourse(@Body body: LearningPathwayCourseAPIBody): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @DELETE("api/v1/pathways")
    suspend fun deleteCourse(@Body body: LearningPathwayCourseAPIBody): Response<ApiResponseX>
}