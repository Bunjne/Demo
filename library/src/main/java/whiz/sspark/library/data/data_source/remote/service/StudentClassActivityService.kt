package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface StudentClassActivityService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/classgroups/{classGroupId}/posts")
    suspend fun listPosts(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/classgroups/{classGroupId}/online_classes")
    suspend fun listOnlineClasses(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>
}