package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize
import java.util.*

data class Instructor(
        @SerializedName("id") val id: String = "",
        @SerializedName("positionEn") val positionEn: String = "",
        @SerializedName("positionTh") val positionTh: String = "",
        @SerializedName("firstNameEn") val firstNameEn: String = "",
        @SerializedName("firstNameTh") val firstNameTh: String = "",
        @SerializedName("middleNameEn") val middleNameEn: String = "",
        @SerializedName("middleNameTh") val middleNameTh: String = "",
        @SerializedName("lastNameEn") val lastNameEn: String = "",
        @SerializedName("lastNameTh") val lastNameTh: String = "",
        @SerializedName("jobPositin") var jobPosition: String = "Job position",
        @SerializedName("gender") var gender: String = "",
        @SerializedName("imageUrl") var imageUrl: String = "",
        @SerializedName("officeRoom") var officeRoom: String = "",
        @SerializedName("officePhoneNumber") var officePhoneNumber: String = "",
        @SerializedName("officeEmail") var officeEmail: String = "",
        @SerializedName("personalPhoneNumber") var personalPhoneNumber: String = "",
        @SerializedName("personalEmail") var personalEmail: String = "",
        @SerializedName("term") var term: Term = Term(),
) {
        val firstName: String get() = localize(firstNameEn, firstNameTh, firstNameEn, false)
        val middleName: String get() = localize(middleNameEn, middleNameTh, middleNameEn, false)
        val lastName: String get() = localize(lastNameEn, lastNameTh, lastNameEn, false)
        val position: String get() = localize(positionEn, positionTh, positionEn, false)

        val fullName get() = convertToFullName(firstName, middleName, lastName, position)
}

fun Instructor.convertToProfile(): Profile {
        return Profile(
                imageUrl = imageUrl,
                gender = gender,
                fullName = fullName,
                position = position,
                firstName = firstName,
                middleName = middleName,
                lastName = lastName
        )
}