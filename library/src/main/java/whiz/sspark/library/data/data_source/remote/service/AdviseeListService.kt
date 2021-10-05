package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface AdviseeListService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/advisors/advisees")
    suspend fun getAdviseeList(): Response<ApiResponseX>
}