package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX

interface StudentAdvisoryAppointmentService {
    @Headers("Content-Type: application/json")
    @GET("api/v1/advisings/slots")
    suspend fun getPendingAdvisorySlot(@Query("termId") termId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @GET("api/v1/advisors/slots/history")
    suspend fun getHistoryAdvisorySlot(@Query("termId") termId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @POST("api/v1/advisors/slots/{slotId}/reserved")
    suspend fun reserveAdvisorySlot(@Path("slotId") slotId: String): Response<ApiResponseX>

    @Headers("Content-Type: application/json")
    @DELETE("api/v1/advisors/slots/{slotId}/reserved")
    suspend fun cancelAdvisorySlot(@Path("slotId") slotId: String): Response<ApiResponseX>
}