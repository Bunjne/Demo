package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query
import whiz.sspark.library.data.entity.ApiResponseX

interface AdviseeListService {
    @POST("v1/advisors/advisees") //TODO wait confirm path
    suspend fun getAdviseeList(): Response<ApiResponseX>
}