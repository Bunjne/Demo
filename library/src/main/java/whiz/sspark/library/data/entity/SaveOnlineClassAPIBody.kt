package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class SaveOnlineClassAPIBody (
    @SerializedName("displayName") val displayName: String,
    @SerializedName("url") val url: String,
    @SerializedName("isGeneratedUrl") val isGeneratedUrl: Boolean,
    @SerializedName("isShown") val isShown: Boolean
)