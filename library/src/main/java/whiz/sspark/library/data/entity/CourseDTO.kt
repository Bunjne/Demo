package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class CourseDTO(
    @SerializedName("id") val id: String,
    @SerializedName("code") val code: String,
    @SerializedName("credit") val credit: Int,
    @SerializedName("nameEn") val nameEn: String,
    @SerializedName("nameTh") val nameTh: String
) {
    val name get() = localize(nameEn, nameTh, nameTh, false)
}
