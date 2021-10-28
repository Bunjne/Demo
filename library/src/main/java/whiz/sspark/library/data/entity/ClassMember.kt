package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize

data class ClassMember(
    @SerializedName("code") val code: String = "",
    @SerializedName("id") val id: String? = null,
    @SerializedName("firstNameEn") val _firstNameEn: String = "",
    @SerializedName("firstNameTh") private val _firstNameTh: String = "",
    @SerializedName("middleNameEn") val _middleNameEn: String = "",
    @SerializedName("middleNameTh") val _middleNameTh: String = "",
    @SerializedName("lastNameEn") val _lastNameEn: String = "",
    @SerializedName("lastNameTh") private val _lastNameTh: String = "",
    @SerializedName("nickNameEn") val _nicknameEn: String = "",
    @SerializedName("nickNameTh") val _nicknameTh: String = "",
    @SerializedName("positionEn") private val positionEn: String = "",
    @SerializedName("positionTh") private val positionTh: String = "",
    @SerializedName("facultyNameEn") private val _facultyNameEn: String = "",
    @SerializedName("facultyNameTh") private val _facultyNameTh: String = "",
    @SerializedName("imageUrl") val imageUrl: String = "",
    @SerializedName("colorCode") val colorCode: String? = null,
    @SerializedName("gender") val gender: String = "",
    @SerializedName("number") val number: Int? = null,
    @SerializedName("jobPosition") val jobPosition: String = "",
    @SerializedName("personalPhoneNumber") val personalPhoneNumber: String = "",
    @SerializedName("personalEmail") val personalEmail: String = "",
    @SerializedName("jobDescription") val jobDescription: String? = null,
) {
    val firstName: String get() = localize(_firstNameEn, _firstNameTh, _firstNameEn, true)
    val middleName get() = localize(_middleNameEn, _middleNameTh, _middleNameEn, false)
    val lastName: String get() = localize(_lastNameEn, _lastNameTh, _lastNameEn, true)
    val nickname: String get() = localize(_nicknameEn, _nicknameTh, _nicknameEn, false)
    val position: String get() = localize(positionEn, positionTh, positionEn, false)
    val fullName: String get() = convertToFullName(firstName, middleName, lastName)
}