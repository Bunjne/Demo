package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

class RefreshTokenAPIBody(
    @SerializedName("userId") val userId: String,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("refreshToken") val refreshToken: String
)