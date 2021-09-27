package whiz.sspark.library.data.entity

import android.content.Context
import com.google.gson.annotations.SerializedName
import whiz.sspark.library.R
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize

data class StudentInstructorDTO(
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

    val fullName get() = convertToFullName(firstName, middleName, lastName, position)
}

fun StudentInstructorDTO.getAdvisorMenuInfoItem(context: Context): MenuContactInfoItem {
    with(this) {
        val contactInfoItems = mutableListOf<MenuContactItem>()

        if (personalPhoneNumber.isNotEmpty()) {
            val personalPhone = MenuContactItem(
                contactIconRes = R.drawable.ic_phone,
                contact = personalPhoneNumber,
                contactDescription = context.resources.getString(R.string.menu_contact_info_personal_phone_text)
            )

            contactInfoItems.add(personalPhone)
        }

        if (officePhoneNumber.isNotEmpty()) {
            val officePhone = MenuContactItem(
                contactIconRes = R.drawable.ic_phone,
                contact = officePhoneNumber,
                contactDescription = context.resources.getString(R.string.menu_contact_info_office_phone_text)
            )

            contactInfoItems.add(officePhone)
        }

        if (officeEmail.isNotEmpty()) {
            val officeEmail = MenuContactItem(
                contactIconRes = R.drawable.ic_at_sign,
                contact = officeEmail,
                contactDescription = context.resources.getString(R.string.menu_contact_info_office_email_text)
            )

            contactInfoItems.add(officeEmail)
        }

        return MenuContactInfoItem(
            imageUrl = imageUrl,
            gender = gender,
            description = context.resources.getString(R.string.general_room, officeRoom),
            name = fullName,
            contactInfoItems = contactInfoItems
        )
    }
}