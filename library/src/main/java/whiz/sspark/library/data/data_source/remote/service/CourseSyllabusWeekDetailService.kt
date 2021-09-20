package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface CourseSyllabusWeekDetailService {
    @GET("v1/course") //TODO wait confirm path
    suspend fun getCourseWeekDetail(@Path("courseId") classGroupId: String,
                                    @Path("termId") termId: String): Response<ApiResponseX>
}