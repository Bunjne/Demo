package whiz.sspark.library.data.datasource.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.SaveOnlineClassAPIBody

interface InstructorClassActivityService {
    @GET("v3/classgroups/{classGroupId}/posts")
    suspend fun listPosts(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>

    @GET("v3/classgroups/{classGroupId}/online_classes")
    suspend fun listOnlineClasses(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>

    @GET("v3/classgroups/{classGroupId}/online_class/microsoft_team_url")
    suspend fun getMSTeamsUrl(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>

    @POST("v3/classgroups/{classGroupId}/online_class/{onlineClassPlatformId}/update")
    suspend fun saveOnlineClass(@Body saveOnlineClassAPIBody: SaveOnlineClassAPIBody): Response<ApiResponseX>
}