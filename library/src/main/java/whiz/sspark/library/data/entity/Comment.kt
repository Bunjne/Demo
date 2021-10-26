package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class Comment(
    @SerializedName("id") val id: String = "",
    @SerializedName("author") val author: ClassMember = ClassMember(),
    @SerializedName("datetime") val datetime: Date = Date(),
    @SerializedName("message") val message: String = "",
    @SerializedName("role") val role: String = ""
)