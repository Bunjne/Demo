package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import whiz.sspark.library.data.entity.ApiResponseX

interface StudentClassAttendanceService {
    @Headers("Content-Type: application/json", "x-mock-match-request-headers: Authorization")
    @GET("api/v1/classGroups/{classGroupId}/attendances")
    suspend fun getClassAttendance(@Path("classGroupId") classGroupId: String): Response<ApiResponseX>
}