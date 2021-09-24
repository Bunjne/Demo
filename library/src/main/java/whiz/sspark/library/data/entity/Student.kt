package whiz.sspark.library.data.entity

import android.content.Context
import com.google.gson.annotations.SerializedName
import whiz.sspark.library.R
import whiz.sspark.library.data.enum.MenuSegmentType
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize
import whiz.sspark.library.view.widget.menu.menu_contact_info_dialog.MenuContactInfoAdapter

data class Student(
    @SerializedName("advisors") private val _advisors: List<StudentInstructorDTO>? = null,
    @SerializedName("profileImageUrl") var profileImageUrl: String = "",
    @SerializedName("code") val code: String = "",
    @SerializedName("middleNameEn") val _middleNameEn: String = "",
    @SerializedName("middleNameTh") val _middleNameTh: String = "",
    @SerializedName("firstNameEn") val _firstNameEn: String = "",
    @SerializedName("firstNameTh") val _firstNameTh: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("guardians") private val _guardians: List<StudentGuardianDTO>? = null,
    @SerializedName("lastNameEn") val _lastNameEn: String = "",
    @SerializedName("lastNameTh") val _lastNameTh: String = "",
    @SerializedName("term") val term: Term = Term(),
    @SerializedName("userId") val userId: String = ""
) {
    val firstName get() = localize(_firstNameEn, _firstNameTh, _firstNameEn, false)
    val middleName get() = localize(_middleNameEn, _middleNameTh, _middleNameEn, false)
    val lastName get() = localize(_lastNameEn, _lastNameTh, _lastNameEn, false)
    val advisor get() = _advisors ?: listOf()
    val guardians get() = _guardians ?: listOf()

    val fullName get() = convertToFullName(firstName, middleName, lastName)
}

fun Student.convertToProfile(): Profile {
    return Profile(
        imageUrl = profileImageUrl,
        gender = gender,
        code = code,
        fullName = fullName,
        firstName = firstName,
        middleName = middleName,
        lastName = lastName
    )
}

fun StudentGuardianDTO.getGuardianMenuInfoItem(context: Context): MenuContactInfoItem {

    with(this) {
        val contactInfoItems = mutableListOf<MenuContactInfoAdapter.MenuContactItem>()

        if (personalPhoneNumber.isNotEmpty()) {
            val personalPhone = MenuContactInfoAdapter.MenuContactItem(
                contactIconRes = R.drawable.ic_phone,
                contact = personalPhoneNumber,
                contactDescription = context.resources.getString(R.string.menu_contact_info_personal_phone_text)
            )
            contactInfoItems.add(personalPhone)
        }

        if (personalEmail.isNotEmpty()) {
            val personalEmail = MenuContactInfoAdapter.MenuContactItem(
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
            personalPhone = personalPhoneNumber,
            personalEmail = personalEmail,
            contactInfoItems = contactInfoItems
        )
    }
}

fun StudentInstructorDTO.getAdvisorMenuInfoItem(context: Context): MenuContactInfoItem {

    with(this) {
        val contactInfoItems = mutableListOf<MenuContactInfoAdapter.MenuContactItem>()

        if (personalPhoneNumber.isNotEmpty()) {
            val personalPhone = MenuContactInfoAdapter.MenuContactItem(
                contactIconRes = R.drawable.ic_phone,
                contact = personalPhoneNumber,
                contactDescription = context.resources.getString(R.string.menu_contact_info_personal_phone_text)
            )
            contactInfoItems.add(personalPhone)
        }

        if (officePhoneNumber.isNotEmpty()) {
            val officePhone = MenuContactInfoAdapter.MenuContactItem(
                contactIconRes = R.drawable.ic_phone,
                contact = officePhoneNumber,
                contactDescription = context.resources.getString(R.string.menu_contact_info_office_phone_text)
            )
            contactInfoItems.add(officePhone)
        }

        if (officeEmail.isNotEmpty()) {
            val officeEmail = MenuContactInfoAdapter.MenuContactItem(
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
            officePhone = officePhoneNumber,
            personalPhone = personalPhoneNumber,
            officeEmail = officeEmail,
            contactInfoItems = contactInfoItems
        )
    }
}

fun Student.getMenuMember(context: Context): List<MenuMemberItem> {
    val menuMemberItems: MutableList<MenuMemberItem> = mutableListOf()

    advisor.forEachIndexed { index, studentInstructorInfo ->
        menuMemberItems.add(
            MenuMemberItem(
                type = MenuSegmentType.INSTRUCTOR,
                index = index,
                imageUrl = studentInstructorInfo.imageUrl,
                gender = studentInstructorInfo.gender,
                description = context.resources.getString(R.string.general_room, studentInstructorInfo.officeRoom),
                name = studentInstructorInfo.fullName
            )
        )
    }

    guardians.forEachIndexed { index, studentInstructorInfo ->
        menuMemberItems.add(
            MenuMemberItem(
                type = MenuSegmentType.GUARDIAN,
                index = index,
                imageUrl = studentInstructorInfo.imageUrl,
                gender = studentInstructorInfo.gender,
                description = studentInstructorInfo.relation,
                name = studentInstructorInfo.fullName
            )
        )
    }

    return menuMemberItems
}