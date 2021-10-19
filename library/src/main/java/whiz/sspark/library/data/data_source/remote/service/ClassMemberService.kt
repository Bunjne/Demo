package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface ClassMemberService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/classgroups/{classGroupId}/members")
    suspend fun getClassMember(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>
}