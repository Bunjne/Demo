package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize
import java.util.*

data class ExpectOutcomeDTO(
    @SerializedName("code") val code: String = "",
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("nameCn") val nameCn: String = "",
    @SerializedName("isCompleted") val isCompleted: Boolean = false,
    @SerializedName("credits") val credits: Int = 0,
    @SerializedName("value") val value: Float? = null,
    @SerializedName("fullValue") val fullValue: Float = 0f,
    @SerializedName("colorCode1") val colorCode1: String = "",
    @SerializedName("colorCode2") val colorCode2: String = "",
    @SerializedName("term") val term: Term? = null,
    @SerializedName("instructorComment") val instructorComment: InstructorCommentDTO? = null,
    @SerializedName("outcomes") private val _outcomes: List<OutcomeDTO>? = null,
) {
    val name get() = localize(nameEn, nameTh, nameCn, false)
    val outcomes get() = _outcomes ?: listOf()
}

data class InstructorCommentDTO(
    @SerializedName("profileImageUrl") val profileImageUrl: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("positionTh") val positionTh: String = "",
    @SerializedName("positionEn") val positionEn: String = "",
    @SerializedName("firstNameTh") val firstNameTh: String = "",
    @SerializedName("firstNameEn") val firstNameEn: String = "",
    @SerializedName("lastNameTh") val lastNameTh: String = "",
    @SerializedName("lastNameEn") val lastNameEn: String = "",
    @SerializedName("middleNameTh") val middleNameTh: String = "",
    @SerializedName("middleNameEn") val middleNameEn: String = "",
    @SerializedName("dateTime") val dateTime: Date = Date(),
    @SerializedName("comment") val comment: String = ""
) {
    val firstName: String get() = localize(firstNameEn, firstNameTh, firstNameEn, false)
    val lastName: String get() = localize(lastNameEn, lastNameTh, lastNameEn, false)
    val middleName: String get() = localize(middleNameEn, middleNameTh, middleNameEn, false)
    val position: String get() = localize(positionEn, positionTh, positionEn, false)

    val fullName get() = convertToFullName(firstName, middleName, lastName, position)

    fun convertToAdapterItem(): InstructorCommentItem {
        return InstructorCommentItem(
            name = fullName,
            createdAt = dateTime,
            comment = comment,
            gender = gender,
            imageUrl = profileImageUrl
        )
    }
}

data class OutcomeDTO(
    @SerializedName("code") val code: String = "",
    @SerializedName("descriptionEn") private val descriptionEn: String = "",
    @SerializedName("descriptionTh") private val descriptionTh: String = "",
    @SerializedName("value") val value: Float? = null,
    @SerializedName("fullValue") val fullValue: Float = 0f,
) {
    val description get() = localize(descriptionEn, descriptionTh, descriptionEn, false)
}