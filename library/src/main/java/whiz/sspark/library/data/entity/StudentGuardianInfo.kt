package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize

data class StudentGuardianInfo(
    @SerializedName("gender") val gender: String = "",
    @SerializedName("firstNameEn") val firstNameEn: String = "",
    @SerializedName("firstNameTh") val firstNameTh: String = "",
    @SerializedName("lastNameEn") val lastNameEn: String = "",
    @SerializedName("lastNameTh") val lastNameTh: String = "",
    @SerializedName("middleNameEn") val middleNameEn: String = "",
    @SerializedName("middleNameTh") val middleNameTh: String = "",
    @SerializedName("relation") val relation: String = "",
    @SerializedName("personalEmail") val personalEmail: String = "",
    @SerializedName("personalPhoneNumber") val personalPhoneNumber: String = "",
    @SerializedName("imageUrl") var imageUrl: String = "",
) {
    val firstName: String get() = localize(firstNameEn, firstNameTh, firstNameEn, false)
    val lastName: String get() = localize(lastNameEn, lastNameTh, lastNameEn, false)
    val middleName: String get() = localize(middleNameEn, middleNameTh, middleNameEn, false)

    val fullName get() = convertToFullName(firstName, middleName, lastName)
}