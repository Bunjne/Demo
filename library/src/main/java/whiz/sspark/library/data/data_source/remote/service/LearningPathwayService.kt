package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.LearningPathwayAddCourseAPIBody

interface LearningPathwayService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/students/me/course") //TODO wait confirm path
    suspend fun getLearningPathway(): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @POST("api/v1/students/me/concentrate_course") //TODO wait confirm path
    suspend fun addCourse(@Body body: LearningPathwayAddCourseAPIBody): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @DELETE("api/v1/students/me/course") //TODO wait confirm path
    suspend fun deleteCourse(@Query("termId") termId: String,
                             @Query("courseId") courseId: String): Response<ApiResponseX>
}