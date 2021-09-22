package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.LearningPathwayAddCourseAPIBody

interface LearningPathwayService {
    @GET("v1/student/me/course") //TODO wait confirm path
    suspend fun getLearningPathway(): Response<ApiResponseX>

    @POST("v1/student/me/concentrate_course") //TODO wait confirm path
    suspend fun addCourse(@Body body: LearningPathwayAddCourseAPIBody): Response<ApiResponseX>

    @DELETE("v1/student/me/course") //TODO wait confirm path
    suspend fun deleteCourse(@Query("termId") termId: String,
                             @Query("courseId") courseId: String): Response<ApiResponseX>
}