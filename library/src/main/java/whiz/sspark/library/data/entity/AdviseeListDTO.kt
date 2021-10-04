package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize

data class AdviseeListDTO(
    @SerializedName("isJunior") val isJunior: Boolean,
    @SerializedName("students") private val _students: List<AdviseeListStudentDTO>? = null
) {
    val students get() = _students ?: listOf()
}

data class AdviseeListStudentDTO(
    @SerializedName("imageUrl") var imageUrl: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("id") val id: String = "",
    @SerializedName("code") val code: String = "",
    @SerializedName("firstNameEn") val _firstNameEn: String = "",
    @SerializedName("firstNameTh") val _firstNameTh: String = "",
    @SerializedName("middleNameEn") val _middleNameEn: String = "",
    @SerializedName("middleNameTh") val _middleNameTh: String = "",
    @SerializedName("lastNameEn") val _lastNameEn: String = "",
    @SerializedName("lastNameTh") val _lastNameTh: String = "",
    @SerializedName("nickNameEn") val _nickNameEn: String = "",
    @SerializedName("nickNameTh") val _nickNameTh: String = "",
    @SerializedName("number") val number: Int? = null,
    @SerializedName("credit") val credit: Int = 0,
    @SerializedName("totalCredit") val totalCredit: Int = 0,
    @SerializedName("GPA") val GPA: Float = 0f,
    @SerializedName("term") val term: Term = Term(),
    @SerializedName("userId") val userId: String = ""
) {
    val firstName get() = localize(_firstNameEn, _firstNameTh, _firstNameEn, false)
    val middleName get() = localize(_middleNameEn, _middleNameTh, _middleNameEn, false)
    val lastName get() = localize(_lastNameEn, _lastNameTh, _lastNameEn, false)

    val fullName get() = convertToFullName(firstName, middleName, lastName)
}
