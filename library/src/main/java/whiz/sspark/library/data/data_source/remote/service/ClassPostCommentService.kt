package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface ClassPostCommentService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/classgroups/posts/{postId}/comments")
    suspend fun listComments(@Path("postId") postId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/classgroups/{classId}/members")
    suspend fun listClassMembers(@Path("classId") classId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @DELETE("api/v1/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: String): Response<ApiResponseX>
}