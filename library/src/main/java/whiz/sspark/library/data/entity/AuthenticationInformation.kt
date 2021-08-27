package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class AuthenticationInformation(
    @SerializedName("accessToken") var accessToken: String = "",
    @SerializedName("refreshToken") var refreshToken: String = "",
    @SerializedName("role") var role: String = ""
)