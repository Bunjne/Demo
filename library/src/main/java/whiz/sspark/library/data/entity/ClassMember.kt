package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.extension.toFirstCharacter
import whiz.sspark.library.utility.localize
import java.util.*

data class ClassMember(
    @SerializedName("code") val code: String = "",
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("classMemberSectionInfo") val classMemberSectionInfo: ClassSharedSection? = null,
    @SerializedName("firstNameEn") val _firstNameEn: String = "",
    @SerializedName("firstNameTh") private val _firstNameTh: String = "",
    @SerializedName("lastNameEn") val _lastNameEn: String = "",
    @SerializedName("lastNameTh") private val _lastNameTh: String = "",
    @SerializedName("nicknameEn") val _nicknameEn: String = "",
    @SerializedName("nicknameTh") val _nicknameTh: String = "",
    @SerializedName("positionEn") private val positionEn: String = "",
    @SerializedName("positionTh") private val positionTh: String = "",
    @SerializedName("facultyNameEn") private val _facultyNameEn: String = "",
    @SerializedName("facultyNameTh") private val _facultyNameTh: String = "",
    @SerializedName("profileImageUrl") val profileImageUrl: String = "",
    @SerializedName("cardImage") val cardImage: String? = "",
    @SerializedName("colorCode") val colorCode: String? = null,
    @SerializedName("gender") val gender: String = "",
    @SerializedName("healthStatus") val healthStatus: String = "",
    @SerializedName("healthCheckedAt") val healthCheckedAt: Date = Date(),
    @SerializedName("number") val number: String = ""
) {
    val firstName: String get() = localize(_firstNameEn, _firstNameTh, _firstNameEn, true)
    val lastName: String get() = localize(_lastNameEn, _lastNameTh, _lastNameEn, true)
    val nickname: String get() = localize(_nicknameEn, _nicknameTh, _nicknameEn, true)
    val facultyName: String get() = localize(_facultyNameEn, _facultyNameTh, _facultyNameEn, true)
    val position: String get() = localize(positionEn, positionTh, positionEn, false)
    val abbreviatedName: String get() = firstName.toFirstCharacter() + lastName.toFirstCharacter()

}