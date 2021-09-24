package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class ConcentrateCourseDTO(
    @SerializedName("nameEn") val nameEn: String,
    @SerializedName("nameTh") val nameTh: String,
    @SerializedName("courses") private val _courses: List<CourseDTO>?
) {
    val name get() = localize(nameEn, nameTh, nameTh, false)
    val courses get() = _courses ?: listOf()
}
