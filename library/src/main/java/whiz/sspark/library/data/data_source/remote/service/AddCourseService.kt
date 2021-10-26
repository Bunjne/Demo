package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX

interface AddCourseService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/pathways/courses")
    suspend fun getConcentrateCourse(): Response<ApiResponseX>
}