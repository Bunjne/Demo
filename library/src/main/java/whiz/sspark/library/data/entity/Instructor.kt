package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize
import java.util.*

data class Instructor(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("userId") val userId: String = "",
        @SerializedName("code") val code: String = "",
        @SerializedName("firstNameEn") val firstNameEn: String = "",
        @SerializedName("firstNameTh") val firstNameTh: String = "",
        @SerializedName("lastNameEn") val lastNameEn: String = "",
        @SerializedName("lastNameTh") val lastNameTh: String = "",
        @SerializedName("fullNameEn") val fullNameEn: String = "",
        @SerializedName("fullNameTh") val fullNameTh: String = "",
        @SerializedName("facultyImageUrl") val facultyImageUrl: String = "",
        @SerializedName("facultyNameEn") val facultyNameEn: String = "",
        @SerializedName("facultyNameTh") val facultyNameTh: String = "",
        @SerializedName("departmentNameEn") val departmentNameEn: String = "",
        @SerializedName("departmentNameTh") val departmentNameTh: String = "",
        @SerializedName("profileImageUrl") var profileImageUrl: String = "",
        @SerializedName("cardImageUrl") val cardImageUrl: String = "",
        @SerializedName("facultyLogoUrl") val facultyLogoUrl: String = "",
        @SerializedName("universityNameEn") val universityNameEn: String = "",
        @SerializedName("universityNameTh") val universityNameTh: String = "",
        @SerializedName("universityLogoUrl") val universityLogoUrl: String = "",
        @SerializedName("colorCode") val colorCode: String? = null,
        @SerializedName("contactRemark") val contactRemark: String = "",
        @SerializedName("officePhone") val officePhone: String = "",
        @SerializedName("room") val room: String = "",
        @SerializedName("nationCode") val nationCode: String = "",
        @SerializedName("typeEn") val typeEn: String = "",
        @SerializedName("typeTh") val typeTh: String = "",
        @SerializedName("gender") val gender: String = "",
        @SerializedName("positionEn") val positionEn: String = "",
        @SerializedName("positionTh") val positionTh: String = "",
        @SerializedName("totalWorkingHours") val totalWorkingHours: Double = 0.0,
        @SerializedName("instructorEmail") val instructorEmail: String = "",
        @SerializedName("personalEmail") var personalEmail: String = "",
        @SerializedName("personalPhone") var personalPhone: String = "",
        @SerializedName("today") var today: Date = Date()
) {
        val firstName: String get() = localize(firstNameEn, firstNameTh, firstNameEn, false)
        val lastName: String get() = localize(lastNameEn, lastNameTh, lastNameEn, false)
        val fullName: String get() = localize(fullNameEn, fullNameTh, fullNameEn, false)
        val position: String get() = localize(positionEn, positionTh, positionEn, false)
}

fun Instructor.convertToProfile(): Profile {
        return Profile(this.profileImageUrl, this.gender, this.code, this.firstName, this.lastName)
}