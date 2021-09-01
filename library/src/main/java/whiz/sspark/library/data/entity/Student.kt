package whiz.sspark.library.data.entity

import android.content.Context
import com.google.gson.annotations.SerializedName
import whiz.sspark.library.R
import whiz.sspark.library.data.enum.MenuSegmentType
import whiz.sspark.library.utility.localize
import java.util.*

data class Student(
    @SerializedName("advisors") private val _advisors: List<StudentInstructorInfo>? = null,
    @SerializedName("birthDate") val date: Date? = null,
    @SerializedName("cardImageUrl") var cardImageUrl: String = "",
    @SerializedName("code") val code: String = "",
    @SerializedName("credit") val credit: Int = 0,
    @SerializedName("citizenId") val citizenId: String = "",
    @SerializedName("expiredAt") val expiredAt: Date? = null,
    @SerializedName("firstNameEn") val _firstNameEn: String = "",
    @SerializedName("firstNameTh") val _firstNameTh: String = "",
    @SerializedName("firstNameCn") val _firstNameCn: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("gpa") val gpa: Double = 0.0,
    @SerializedName("guardians") private val _guardians: List<StudentGuardianInfo>? = null,
    @SerializedName("id") val id: Long = 0L,
    @SerializedName("issuedAt") val issuedAt: Date? = null,
    @SerializedName("lastNameEn") val _lastNameEn: String = "",
    @SerializedName("lastNameTh") val _lastNameTh: String = "",
    @SerializedName("lastNameCn") val _lastNameCn: String = "",
    @SerializedName("phoneNumber") val phoneNumber: String = "",
    @SerializedName("profileImageUrl") var profileImageUrl: String = "",
    @SerializedName("schoolNameEn") val schoolNameEn: String = "",
    @SerializedName("schoolNameTh") val schoolNameTh: String = "",
    @SerializedName("schoolLogoUrl") val schoolLogoUrl: String = "",
    @SerializedName("today") var today: Date = Date(),
    @SerializedName("userId") val userId: String = "",
) {
    val firstName get() = localize(_firstNameEn, _firstNameTh, _firstNameCn, false)
    val lastName get() = localize(_lastNameEn, _lastNameTh, _lastNameCn, false)
    val advisor get() = _advisors ?: listOf()
    val guardians get() = _guardians ?: listOf()
}

fun Student.convertToProfile(): Profile {
    return Profile(this.profileImageUrl, this.gender, this.firstName, this.lastName)
}

fun Student.getMenuMember(context: Context): List<MenuMember> {
    val member: MutableList<MenuMember> = mutableListOf()
    val instructor = advisor.map { MenuMember(type = MenuSegmentType.INSTRUCTOR, imageUrl = it.profileImageUrl, gender = it.gender, description = context.resources.getString(R.string.general_room, it.officeRoom), name = it.fullName) }
    val guardians = guardians.map { MenuMember(type = MenuSegmentType.GUARDIAN, imageUrl = it.profileImageUrl, gender = it.gender, description = it.relation, name = it.fullName) }
    member.addAll(instructor)
    member.addAll(guardians)
    return member
}