package whiz.sspark.library.data.entity

import android.content.Context
import com.google.gson.annotations.SerializedName
import whiz.sspark.library.R
import whiz.sspark.library.data.enum.MenuSegmentType
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize
import java.util.*

data class Student(
    @SerializedName("advisors") private val _advisors: List<StudentInstructorInfo>? = null,
    @SerializedName("imageUrl") var imageUrl: String = "",
    @SerializedName("code") val code: String = "",
    @SerializedName("middleNameEn") val _middleNameEn: String = "",
    @SerializedName("middleNameTh") val _middleNameTh: String = "",
    @SerializedName("firstNameEn") val _firstNameEn: String = "",
    @SerializedName("firstNameTh") val _firstNameTh: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("guardians") private val _guardians: List<StudentGuardianInfo>? = null,
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

fun Student.getMenuMember(context: Context): List<MenuMember> {
    val member: MutableList<MenuMember> = mutableListOf()

    val instructors = advisor.map { MenuMember(type = MenuSegmentType.INSTRUCTOR, imageUrl = it.imageUrl, gender = it.gender, description = context.resources.getString(R.string.general_room, it.officeRoom), name = it.fullName) }
    member.addAll(instructors)

    val guardians = guardians.map { MenuMember(type = MenuSegmentType.GUARDIAN, imageUrl = it.imageUrl, gender = it.gender, description = it.relation, name = it.fullName) }
    member.addAll(guardians)

    return member
}