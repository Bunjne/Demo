package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface LikeBySeenByService {
    @GET("v3/classgroups/{classGroupId}/members")
    suspend fun listClassMembers(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>

    @GET("/v3/posts/{postId}/liked_by")
    suspend fun getUserIdsByPostLiked(@Path("postId") postId: String): Response<ApiResponseX>

    @GET("/v3/posts/{postId}/seen_by")
    suspend fun getUserIdsByPostSeen(@Path("postId") postId: String): Response<ApiResponseX>
}