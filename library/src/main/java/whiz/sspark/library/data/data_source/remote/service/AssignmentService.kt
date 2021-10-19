package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface AssignmentService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/classGroups/assignments")
    suspend fun getAssignment(@Query("termId") termId: String,
                              @Query("page") page: Int,
                              @Query("pageSize") pageSize: Int): Response<ApiResponseX>
}