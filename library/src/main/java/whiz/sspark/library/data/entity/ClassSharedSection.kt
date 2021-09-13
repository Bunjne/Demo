package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class ClassSharedSection(
        @SerializedName("sectionId") val sectionId: Long = 0L,
        @SerializedName("courseCode") val courseCode: String = "",
        @SerializedName("courseNameEn") val _courseNameEn: String = "",
        @SerializedName("courseNameTh") val _courseNameTh: String = "",
        @SerializedName("sectionNumber") val sectionNumber: String = ""
) {
        val courseName: String get() = localize(_courseNameEn, _courseNameTh, _courseNameEn)
}