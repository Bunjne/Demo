package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface NotificationInboxService {
    @GET("v1/notifications")
    suspend fun getNotification(@Query("page") page: Int,
                                @Query("pageSize") pageSize: Int): Response<ApiResponseX>
}