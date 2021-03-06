package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface LikeBySeenByService {
    @GET("api/v1/classgroups/{classGroupId}/posts/{postId}/likes")
    suspend fun getMembersByPostLiked(@Path("classGroupId") classGroupId: String, @Path("postId") postId: String): Response<ApiResponseX>

    @GET("api/v1/classgroups/{classGroupId}/posts/{postId}/reads")
    suspend fun getMembersByPostSeen(@Path("classGroupId") classGroupId: String, @Path("postId") postId: String): Response<ApiResponseX>
}