package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface CourseSyllabusDetailService {
    @GET("v1/courses/{courseId}") //TODO wait confirm path
    suspend fun getCourseDetail(@Path("courseId") classGroupId: String): Response<ApiResponseX>
}