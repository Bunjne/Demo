package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

class LoginAPIBody(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("operator") val operator: String
)