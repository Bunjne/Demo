package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("data") val data: String? = null,
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("error") val error: ApiErrorResponse? = null
)