package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX

interface AdviseeAbilityService: AbilityService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/students/{studentId}/abilities")
    suspend fun getAbility(@Path("studentId") studentId: String,
                           @Query("termId") termId: String): Response<ApiResponseX>
}