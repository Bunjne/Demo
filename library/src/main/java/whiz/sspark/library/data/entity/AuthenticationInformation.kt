package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class AuthenticationInformation(
    @SerializedName("access_token") val accessToken: String = "",
    @SerializedName("expires_in") val expires_in: Long = 0,
    @SerializedName("token_type") val token_type: String = "",
    @SerializedName("refresh_token") val refreshToken: String = "",
    @SerializedName("scope") val scope: String = ""
)