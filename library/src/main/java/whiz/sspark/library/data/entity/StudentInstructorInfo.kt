package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.convertToFullNameWithPosition
import whiz.sspark.library.utility.localize

data class StudentInstructorInfo(
        @SerializedName("code") val code: String = "",
        @SerializedName("gender") val gender: String = "",
        @SerializedName("firstNameEn") val firstNameEn: String = "",
        @SerializedName("firstNameTh") val firstNameTh: String = "",
        @SerializedName("middleNameEn") val middleNameEn: String = "",
        @SerializedName("middleNameTh") val middleNameTh: String = "",
        @SerializedName("lastNameEn") val lastNameEn: String = "",
        @SerializedName("lastNameTh") val lastNameTh: String = "",
        @SerializedName("officeEmail") val officeEmail: String = "",
        @SerializedName("officeRoom") val officeRoom: String = "",
        @SerializedName("officePhoneNumber") val officePhoneNumber: String = "",
        @SerializedName("personalPhoneNumber") var personalPhoneNumber: String = "",
        @SerializedName("imageUrl") var imageUrl: String = "",
        @SerializedName("positionEn") val positionEn: String = "",
        @SerializedName("positionTh") val positionTh: String = "",
) {
        val firstName: String get() = localize(firstNameEn, firstNameTh, firstNameEn, false)
        val lastName: String get() = localize(lastNameEn, lastNameTh, lastNameEn, false)
        val middleName: String get() = localize(middleNameEn, middleNameTh, middleNameEn, false)
        val position: String get() = localize(positionEn, positionTh, positionEn, false)

        val fullName get() = convertToFullNameWithPosition(position, firstName, middleName, lastName)
}