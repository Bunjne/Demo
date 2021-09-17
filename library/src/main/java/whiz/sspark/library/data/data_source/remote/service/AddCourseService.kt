package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.LearningPathwayAddCourseAPIBody

interface AddCourseService {
    @GET("v1/student/me/concentrate_course") //TODO wait confirm path
    suspend fun getCourses(): Response<ApiResponseX>
}