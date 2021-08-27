package whiz.sspark.library.data.datasource.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.Student

interface ProfileService {
    @GET("v1/students/me")
    suspend fun getProfile(): Response<Student>
}