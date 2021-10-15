package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.AddCommentAPIBody
import whiz.sspark.library.data.entity.ApiResponseX

interface ClassPostCommentService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/classgroups/posts/{postId}/comments")
    suspend fun listComments(@Path("postId") postId: String): Response<ApiResponseX>

    @Headers("Content-Type: multipart/form-data")
    @POST("api/v1/classgroups/posts/{postId}/comments")
    suspend fun addComment(@Path("postId") postId: String, @Body addCommentAPIBody: AddCommentAPIBody): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @DELETE("api/v1/classgroups/posts/{postId}/comments/{commentId}")
    suspend fun deleteComment(@Path("postId") postId: String,
                              @Path("commentId") commentId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/classgroups/{classGroupId}/members")
    suspend fun listClassMembers(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @DELETE("api/v1/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: String): Response<ApiResponseX>
}