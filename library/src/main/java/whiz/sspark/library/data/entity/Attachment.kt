package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.io.File

data class Attachment(
    @SerializedName("name") val name: String = "",
    @SerializedName("url") val url: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("extensions") val extension: String = "",
    @SerializedName("file") var file: File? = null,
    @SerializedName("isLocal") var isLocal: Boolean = false
)