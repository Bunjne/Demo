package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface StudentClassActivityService {
    @GET("v3/classgroups/{classGroupId}/posts")
    suspend fun listPosts(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>

    @GET("v3/classgroups/{classGroupId}/online_classes")
    suspend fun listOnlineClasses(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>
}