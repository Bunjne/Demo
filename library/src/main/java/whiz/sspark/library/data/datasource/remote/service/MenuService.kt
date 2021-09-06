package whiz.sspark.library.data.datasource.remote.service

import retrofit2.Response
import retrofit2.http.GET
import whiz.sspark.library.data.entity.ApiResponseX

interface MenuService {
    @GET("v1/menu")
    suspend fun getMenu(): Response<ApiResponseX>
}