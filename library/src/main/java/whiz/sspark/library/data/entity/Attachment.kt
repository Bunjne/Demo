package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.io.File

data class Attachment(
    @SerializedName("Name") val name: String = "",
    @SerializedName("Url") val url: String = "",
    @SerializedName("Type") val type: String = "",
    @SerializedName("Extension") val extension: String = "",
    @SerializedName("file") var file: File? = null,
    @SerializedName("isLocal") var isLocal: Boolean = false
)