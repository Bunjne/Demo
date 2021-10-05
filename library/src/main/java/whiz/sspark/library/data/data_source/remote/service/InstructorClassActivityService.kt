package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.SaveOnlineClassAPIBody

interface InstructorClassActivityService {
    @Headers("Content-Type: application/json")
    @GET("api/v3/classgroups/{classGroupId}/posts")
    suspend fun listPosts(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v3/classgroups/{classGroupId}/online_classes")
    suspend fun listOnlineClasses(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v3/classgroups/{classGroupId}/online_class/microsoft_team_url")
    suspend fun getMSTeamsUrl(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @POST("api/v3/classgroups/{classGroupId}/online_class/{onlineClassPlatformId}/update")
    suspend fun saveOnlineClass(@Body saveOnlineClassAPIBody: SaveOnlineClassAPIBody): Response<ApiResponseX>
}