package whiz.sspark.library.data.entity

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ApiResponseX(
    @SerializedName("code") val code: String = "",
    @SerializedName("message") val message: String = "",
    @SerializedName("data") private val _data: Any? = "",
    @SerializedName("statusCode") var statusCode: Int = 0
) {
    val data: String get() = Gson().toJson(_data)
}