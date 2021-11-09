package whiz.sspark.library.data.data_source.remote.service

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX

interface ManageAssignmentService {
    @POST("api/v1/classGroups/{classGroupId}/assignments")
    suspend fun createAssignment(@Path("classGroupId") classGroupId: String,
                                 @Body requestBody: RequestBody): Response<ApiResponseX>

    @PUT("api/v1/classGroups/{classGroupId}/assignments/{assignmentId}")
    suspend fun updateAssignment(@Path("classGroupId") classGroupId: String,
                                 @Path("assignmentId") assignmentId: String,
                                 @Body requestBody: RequestBody): Response<ApiResponseX>
}