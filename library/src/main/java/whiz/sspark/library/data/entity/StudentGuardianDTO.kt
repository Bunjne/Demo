package whiz.sspark.library.data.entity

import android.content.Context
import com.google.gson.annotations.SerializedName
import whiz.sspark.library.R
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize

data class StudentGuardianDTO(
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

fun StudentGuardianDTO.getGuardianMenuInfoItem(context: Context): MenuContactInfoItem {
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

        if (personalEmail.isNotEmpty()) {
            val personalEmail = MenuContactItem(
                contactIconRes = R.drawable.ic_at_sign,
                contact = personalEmail,
                contactDescription = context.resources.getString(R.string.menu_contact_info_personal_email_text)
            )

            contactInfoItems.add(personalEmail)
        }

        return MenuContactInfoItem(
            imageUrl = imageUrl,
            gender = gender,
            description = relation,
            name = fullName,
            contactInfoItems = contactInfoItems
        )
    }
}