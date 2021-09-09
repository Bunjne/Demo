package whiz.sspark.library.data.entity

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class ApiResponseX(
    @SerializedName("code") val code: String = "",
    @SerializedName("messageEn") val messageEn: String = "",
    @SerializedName("messageTh") val messageTh: String = "",
    @SerializedName("data") private val _data: Any? = "",
    @SerializedName("statusCode") var statusCode: Int = 0
) {
    val message get() = localize(messageEn, messageTh, messageEn, false)
    val data: String get() = Gson().toJson(_data)
}