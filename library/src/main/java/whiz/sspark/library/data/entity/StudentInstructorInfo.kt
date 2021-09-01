package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class StudentInstructorInfo(
        @SerializedName("code") val code: String = "",
        @SerializedName("contactRemark") val contactRemark: String = "",
        @SerializedName("gender") val gender: String = "",
        @SerializedName("firstNameEn") val firstNameEn: String = "",
        @SerializedName("firstNameTh") val firstNameTh: String = "",
        @SerializedName("fullNameEn") val fullNameEn: String = "",
        @SerializedName("fullNameTh") val fullNameTh: String = "",
        @SerializedName("id") val id: Int = 0,
        @SerializedName("lastNameEn") val lastNameEn: String = "",
        @SerializedName("lastNameTh") val lastNameTh: String = "",
        @SerializedName("officeRoom") val officeRoom: String = "",
        @SerializedName("officePhone") val officePhone: String = "",
        @SerializedName("personalPhone") var personalPhone: String = "",
        @SerializedName("profileImageUrl") var profileImageUrl: String = "",
        @SerializedName("positionEn") val positionEn: String = "",
        @SerializedName("positionTh") val positionTh: String = "",
        @SerializedName("schoolEmail") val schoolEmail: String = "",
) {
        val firstName: String get() = localize(firstNameEn, firstNameTh, firstNameEn, false)
        val lastName: String get() = localize(lastNameEn, lastNameTh, lastNameEn, false)
        val fullName: String get() = localize(fullNameEn, fullNameTh, fullNameEn, false)
        val position: String get() = localize(positionEn, positionTh, positionEn, false)
}