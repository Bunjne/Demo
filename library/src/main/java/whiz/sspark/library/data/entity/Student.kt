package whiz.sspark.library.data.entity

import android.content.Context
import com.google.gson.annotations.SerializedName
import whiz.sspark.library.R
import whiz.sspark.library.data.enum.MenuSegmentType
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize

data class Student(
    @SerializedName("id") val id: String = "",
    @SerializedName("advisors") private val _advisors: List<StudentInstructorDTO>? = null,
    @SerializedName("guardians") private val _guardians: List<StudentGuardianDTO>? = null,
    @SerializedName("imageUrl") var imageUrl: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("firstNameEn") val _firstNameEn: String = "",
    @SerializedName("firstNameTh") val _firstNameTh: String = "",
    @SerializedName("middleNameEn") val _middleNameEn: String = "",
    @SerializedName("middleNameTh") val _middleNameTh: String = "",
    @SerializedName("lastNameEn") val _lastNameEn: String = "",
    @SerializedName("lastNameTh") val _lastNameTh: String = "",
    @SerializedName("nickNameEn") val _nickNameEn: String = "",
    @SerializedName("nickNameTh") val _nickNameTh: String = "",
    @SerializedName("code") val code: String = "",
    @SerializedName("number") val number: Int? = null,
    @SerializedName("term") val term: Term = Term(),
    @SerializedName("credit") val credit: Int = 0,
    @SerializedName("totalCredit") val totalCredit: Int = 0,
    @SerializedName("GPA") val GPA: Float = 0f
) {
    val firstName get() = localize(_firstNameEn, _firstNameTh, _firstNameEn, false)
    val middleName get() = localize(_middleNameEn, _middleNameTh, _middleNameEn, false)
    val lastName get() = localize(_lastNameEn, _lastNameTh, _lastNameEn, false)
    val nickName get() = localize(_nickNameEn, _nickNameTh, _nickNameEn, false)
    val advisor get() = _advisors ?: listOf()
    val guardians get() = _guardians ?: listOf()

    val fullName get() = convertToFullName(firstName, middleName, lastName)
}

fun Student.convertToJuniorAdvisee(): Advisee {
    return Advisee(
        nickname = nickName,
        code = number?.toString() ?: "",
        name = fullName,
        imageUrl = imageUrl,
        gender = gender,
        GPA = GPA,
        credit = credit,
        totalCredit = totalCredit
    )
}

fun Student.convertToSeniorAdvisee(): Advisee {
    return Advisee(
        nickname = nickName,
        code = code,
        name = fullName,
        imageUrl = imageUrl,
        gender = gender,
        GPA = GPA,
        credit = credit,
        totalCredit = totalCredit
    )
}


fun Student.convertToProfile(): Profile {
    return Profile(
        imageUrl = imageUrl,
        gender = gender,
        code = code,
        fullName = fullName,
        firstName = firstName,
        middleName = middleName,
        lastName = lastName
    )
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