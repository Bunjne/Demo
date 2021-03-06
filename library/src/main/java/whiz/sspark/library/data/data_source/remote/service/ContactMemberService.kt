package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface ContactMemberService {
    @GET("api/v1/contants/{contactGroupId}/members")
    suspend fun getContactMembers(@Path("contactGroupId") contactGroupId: String): Response<ApiResponseX>
}