package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface ContactService {
    @GET("api/v1/contacts")
    suspend fun getContacts(): Response<ApiResponseX>
}