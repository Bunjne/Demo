package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import whiz.sspark.library.data.entity.ApiResponseX

interface SchoolRecordService {
    @GET("v1/students/me/terms")
    suspend fun getTerms(): Response<ApiResponseX>
}