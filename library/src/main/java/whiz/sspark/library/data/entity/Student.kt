package whiz.sspark.library.data.entity

import android.content.Context
import com.google.gson.annotations.SerializedName
import whiz.sspark.library.R
import whiz.sspark.library.data.enum.MenuSegmentType
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize

data class Student(
    @SerializedName("advisors") private val _advisors: List<StudentInstructorDTO>? = null,
    @SerializedName("imageUrl") var imageUrl: String = "",
    @SerializedName("code") val code: String = "",
    @SerializedName("middleNameEn") val _middleNameEn: String = "",
    @SerializedName("middleNameTh") val _middleNameTh: String = "",
    @SerializedName("firstNameEn") val _firstNameEn: String = "",
    @SerializedName("firstNameTh") val _firstNameTh: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("guardians") private val _guardians: List<StudentGuardianDTO>? = null,
    @SerializedName("lastNameEn") val _lastNameEn: String = "",
    @SerializedName("lastNameTh") val _lastNameTh: String = "",
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
    return Profile(this.imageUrl, this.gender, this.code, this.firstName, this.lastName)
}

fun Student.getGuardianMemberContactInfo(context: Context): MutableList<MenuContactInfoItem> {
    val memberContactInfo = mutableListOf<MenuContactInfoItem>()

    guardians.forEachIndexed { index, studentGuardianContactInfo ->
        memberContactInfo.add(
            MenuContactInfoItem(
                menuSegmentType = MenuSegmentType.GUARDIAN,
                index = index,
                imageUrl = studentGuardianContactInfo.imageUrl,
                gender = studentGuardianContactInfo.gender,
                description = context.resources.getString(R.string.general_relation, studentGuardianContactInfo.relation),
                name = studentGuardianContactInfo.fullName,
                personalPhone = studentGuardianContactInfo.personalPhoneNumber,
                personalEmail = studentGuardianContactInfo.personalEmail
            )
        )
    }

    return memberContactInfo
}

fun Student.getAdvisorMemberContactInfo(context: Context): MutableList<MenuContactInfoItem> {
    val memberContactInfo = mutableListOf<MenuContactInfoItem>()

    advisor.forEachIndexed { index, studentInstructorContactInfo ->
        memberContactInfo.add(
            MenuContactInfoItem(
                menuSegmentType = MenuSegmentType.INSTRUCTOR,
                index = index,
                imageUrl = studentInstructorContactInfo.imageUrl,
                gender = studentInstructorContactInfo.gender,
                description = context.resources.getString(R.string.general_room, studentInstructorContactInfo.officeRoom),
                name = studentInstructorContactInfo.fullName,
                personalPhone = studentInstructorContactInfo.personalPhoneNumber,
                officePhone = studentInstructorContactInfo.officePhoneNumber,
                officeEmail = studentInstructorContactInfo.officeEmail
            )
        )
    }

    return memberContactInfo
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