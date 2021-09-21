package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface AbilityService {
    @POST("v1/student/me/ability") //TODO wait confirm path
    suspend fun getAbility(@Query("termId") termId: String): Response<ApiResponseX>
}