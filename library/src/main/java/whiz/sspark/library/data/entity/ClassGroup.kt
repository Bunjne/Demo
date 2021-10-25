package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.utility.localize

data class ClassGroup(
        @SerializedName("specialGroup") val specialGroup: ClassGroupCourse? = null,
        @SerializedName("guardianGroup") val guardianGroup: ClassGroupCourse? = null,
        @SerializedName("seniorClass") private val _seniorClasses: List<ClassGroupDetail>? = null,
        @SerializedName("juniorClass") private val _juniorClasses: List<ClassGroupDetail>? = null
) {
        val seniorClasses get() = _seniorClasses ?: listOf()
        val juniorClasses get() = _juniorClasses ?: listOf()
}

data class ClassGroupDetail(
        @SerializedName("nameEn") private val _classGroupNameEn: String = "",
        @SerializedName("nameTh") private val _classGroupNameTh: String = "",
        @SerializedName("iconImageUrl") val iconImageUrl: String = "",
        @SerializedName("colorCode1") val colorCode1: String = "",
        @SerializedName("colorCode2") val colorCode2: String = "",
        @SerializedName("courses") val courses: List<ClassGroupCourse>
) {
        val classGroupName: String get() = localize(_classGroupNameEn, _classGroupNameTh, _classGroupNameEn)
        val gradientColors: IntArray get() = intArrayOf(colorCode1.toColor(), colorCode2.toColor())
}

data class ClassGroupCourse(
        @SerializedName("classGroupId") val classGroupId: String = "",
        @SerializedName("colorCode1") val colorCode1: String = "",
        @SerializedName("colorCode2") val colorCode2: String = "",
        @SerializedName("code") val courseCode: String = "",
        @SerializedName("nameEn") private val _courseNameEn: String = "",
        @SerializedName("nameTh") private val _courseNameTh: String = "",
        @SerializedName("studentCount") val studentCount: Int = 0,
        @SerializedName("memberCount") val memberCount: Int = 0,
        @SerializedName("sectionNumber") val sectionNumber: String? = null,
        @SerializedName("instructors") val instructors: List<ClassMember> = listOf(),
        @SerializedName("notiBadge") val notificationCount: Int = 0,
) {
        val courseName: String get() = localize(_courseNameEn, _courseNameTh, _courseNameEn)
        val gradientColors: IntArray get() = intArrayOf(colorCode1.toColor(), colorCode2.toColor())
}