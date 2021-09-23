package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize

data class ClassScheduleDTO(
        @SerializedName("code") val code: String = "",
        @SerializedName("nameEn") val nameEn: String = "",
        @SerializedName("nameTh") val nameTh: String = "",
        @SerializedName("day") val day: Int = 0,
        @SerializedName("startTime") val startTime: String = "",
        @SerializedName("endTime") val endTime: String = "",
        @SerializedName("colorCode1") val colorCode1: String = "",
        @SerializedName("colorCode2") val colorCode2: String = "",
        @SerializedName("room") val room: String = "",
        @SerializedName("instructors") private val _instructors: List<ClassScheduleInstructorDTO>? = null
) {
        val name get() = localize(nameEn, nameTh, nameEn, false)
        val instructors get() = _instructors ?: listOf()
}

data class ClassScheduleInstructorDTO(
        @SerializedName("code") val code: String = "",
        @SerializedName("firstNameEn") val firstNameEn: String = "",
        @SerializedName("firstNameTh") val firstNameTh: String = "",
        @SerializedName("middleNameEn") val middleNameEn: String = "",
        @SerializedName("middleNameTh") val middleNameTh: String = "",
        @SerializedName("lastNameEn") val lastNameEn: String = "",
        @SerializedName("lastNameTh") val lastNameTh: String = "",
        @SerializedName("positionEn") val positionEn: String = "",
        @SerializedName("positionTh") val positionTh: String = ""
) {
        val firstName: String get() = localize(firstNameEn, firstNameTh, firstNameEn, false)
        val lastName: String get() = localize(lastNameEn, lastNameTh, lastNameEn, false)
        val middleName: String get() = localize(middleNameEn, middleNameTh, middleNameEn, false)
        val position: String get() = localize(positionEn, positionTh, positionEn, false)

        val fullName get() = convertToFullName(firstName, middleName, lastName, position)
}
