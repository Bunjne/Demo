package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("id") val id: String = "",
    @SerializedName("statusCode") val statusCode: Int = 0,
    @SerializedName("description") val description: String = ""
)