package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class StudentGuardianInfo(
    @SerializedName("id") val id: Long = 0L,
    @SerializedName("code") val code: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("firstNameEn") val firstNameEn: String = "",
    @SerializedName("firstNameTh") val firstNameTh: String = "",
    @SerializedName("firstNameCn") val firstNameCn: String = "",
    @SerializedName("fullNameEn") val fullNameEn: String = "",
    @SerializedName("fullNameTh") val fullNameTh: String = "",
    @SerializedName("fullNameCn") val fullNameCn: String = "",
    @SerializedName("lastNameEn") val lastNameEn: String = "",
    @SerializedName("lastNameTh") val lastNameTh: String = "",
    @SerializedName("lastNameCn") val lastNameCn: String = "",
    @SerializedName("relationEn") val relationEn: String = "",
    @SerializedName("relationTh") val relationTh: String = "",
    @SerializedName("relationCn") val relationCn: String = "",
    @SerializedName("personalEmail") val personalEmail: String = "",
    @SerializedName("contactRemark") val contactRemark: String = "",
    @SerializedName("personalPhone") val personalPhone: String = "",
    @SerializedName("profileImageUrl") var profileImageUrl: String = "",
) {
    val firstName: String get() = localize(firstNameEn, firstNameTh, firstNameCn, false)
    val lastName: String get() = localize(lastNameEn, lastNameTh, lastNameCn, false)
    val fullName: String get() = localize(fullNameEn, fullNameTh, fullNameCn, false)
    val relation: String get() = localize(relationEn, relationTh, relationCn, false)
}