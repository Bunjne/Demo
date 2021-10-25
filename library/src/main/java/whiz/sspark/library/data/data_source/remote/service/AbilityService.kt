package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface AbilityService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/students/me/abilities")
    suspend fun getAbility(@Query("termId") termId: String): Response<ApiResponseX>
}