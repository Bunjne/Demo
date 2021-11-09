package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface ClassAssignmentService {
    @GET("api/v1/classGroups/{classGroupId}/assignments")
    suspend fun getAssignment(@Path("classGroupId") classGroupId: String,
                              @Query("page") page: Int,
                              @Query("pageSize") pageSize: Int): Response<ApiResponseX>
}