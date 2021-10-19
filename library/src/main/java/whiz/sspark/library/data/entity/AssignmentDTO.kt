package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize
import java.util.*

data class AssignmentDTO(
    @SerializedName("totalPage") val totalPage: Int = 0,
    @SerializedName("items") private val _items: List<AssignmentItemDTO>? = null
) {
    val items get() = _items ?: listOf()
}

data class AssignmentItemDTO(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("deadlineAt") val deadlineAt: Date,
    @SerializedName("createdAt") val createdAt: Date,
    @SerializedName("updatedAt") val updatedAt: Date,
    @SerializedName("attachments") private val _attachments: List<Attachment>?,
    @SerializedName("instructor") val instructor: AssignmentInstructorDTO,
    @SerializedName("classGroup") val classGroup: AssignmentClassGroupDTO
) {
    val attachments get() = _attachments ?: listOf()

    fun convertToAssignment() = Assignment(
        startColor = classGroup.colorCode1,
        endColor = classGroup.colorCode2,
        courseName = classGroup.name,
        courseCode = classGroup.code,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deadlineAt = deadlineAt,
        title = title,
        description = message,
        instructorName = instructor.fullName,
        imageUrl = instructor.imageUrl,
        gender = instructor.gender,
        attachments = attachments
    )
}

data class AssignmentClassGroupDTO(
    @SerializedName("classGroupId") val classGroupId: String,
    @SerializedName("colorCode1") val colorCode1: String,
    @SerializedName("colorCode2") val colorCode2: String,
    @SerializedName("code") val code: String,
    @SerializedName("nameEn") val nameEn: String,
    @SerializedName("nameTh") val nameTh: String
) {
    val name: String get() = localize(nameEn, nameTh, nameEn, false)
}

data class AssignmentInstructorDTO(
    @SerializedName("id") val id: String = "",
    @SerializedName("imageUrl") val imageUrl: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("positionTh") val positionTh: String = "",
    @SerializedName("positionEn") val positionEn: String = "",
    @SerializedName("firstNameTh") val firstNameTh: String = "",
    @SerializedName("firstNameEn") val firstNameEn: String = "",
    @SerializedName("lastNameTh") val lastNameTh: String = "",
    @SerializedName("lastNameEn") val lastNameEn: String = "",
    @SerializedName("middleNameTh") val middleNameTh: String = "",
    @SerializedName("middleNameEn") val middleNameEn: String = ""
) {
    val firstName: String get() = localize(firstNameEn, firstNameTh, firstNameEn, false)
    val lastName: String get() = localize(lastNameEn, lastNameTh, lastNameEn, false)
    val middleName: String get() = localize(middleNameEn, middleNameTh, middleNameEn, false)
    val position: String get() = localize(positionEn, positionTh, positionEn, false)

    val fullName get() = convertToFullName(firstName, middleName, lastName, position)
}
