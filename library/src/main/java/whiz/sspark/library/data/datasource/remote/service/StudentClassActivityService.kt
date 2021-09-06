package whiz.sspark.library.data.datasource.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface StudentClassActivityService {
    @GET("v1/classes/{id}/posts")
    suspend fun listPosts(@Path("id") id: Long): Response<ApiResponseX>

    @GET("v2/classes/{classId}/online_classes")
    suspend fun listOnlineClasses(@Path("classId") classId: Long): Response<ApiResponseX>
}