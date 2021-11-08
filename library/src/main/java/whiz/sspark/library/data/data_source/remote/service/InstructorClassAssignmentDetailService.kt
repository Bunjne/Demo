package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX

interface InstructorClassAssignmentDetailService {
    @DELETE("api/v1/classGroups/{classGroupId}/assignments/{assignmentId}")
    suspend fun deleteAssignment(@Path("classGroupId") classGroupId: String,
                                 @Path("assignmentId") assignmentId: String): Response<ApiResponseX>
}