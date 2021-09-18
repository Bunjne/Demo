package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.extension.getFirstConsonant
import whiz.sspark.library.utility.localize
import java.util.*

data class ClassMember(
    @SerializedName("code") val code: String = "",
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("classMemberSectionInfo") val classMemberSectionInfo: ClassSharedSection? = null,
    @SerializedName("firstNameEn") val _firstNameEn: String = "",
    @SerializedName("firstNameTh") private val _firstNameTh: String = "",
    @SerializedName("middleNameEn") val _middleNameEn: String = "",
    @SerializedName("middleNameTh") val _middleNameTh: String = "",
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
    @SerializedName("abbreviateNameEn") val abbreviateNameEn: String = "",
    @SerializedName("abbreviateNameTh") val abbreviateNameTh: String = "",
    @SerializedName("number") val number: String = "",
    @SerializedName("remark") val remark: String = "" //TODO this will be talked with API or Design later, when ClassMember topic is raised to discuss.
) {
    val firstName: String get() = localize(_firstNameEn, _firstNameTh, _firstNameEn, true)
    val middleName get() = localize(_middleNameEn, _middleNameTh, _middleNameEn, false)
    val lastName: String get() = localize(_lastNameEn, _lastNameTh, _lastNameEn, true)
    val nickname: String get() = localize(_nicknameEn, _nicknameTh, _nicknameEn, true)
    val facultyName: String get() = localize(_facultyNameEn, _facultyNameTh, _facultyNameEn, true)
    val position: String get() = localize(positionEn, positionTh, positionEn, false)
    val abbreviatedName: String get() {
        val localizedAbbreviatedName = localize(abbreviateNameEn, abbreviateNameTh, abbreviateNameEn, false)

        return if (localizedAbbreviatedName.isNotBlank()) {
            localizedAbbreviatedName
        } else {
            firstName.getFirstConsonant() + lastName.getFirstConsonant()
        }
    }
}