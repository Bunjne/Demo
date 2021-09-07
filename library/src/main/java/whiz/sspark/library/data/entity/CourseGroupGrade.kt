package whiz.sspark.library.data.entity

import whiz.sspark.library.extension.toColor
import whiz.sspark.library.utility.localize

data class CourseGroupGrade (
    val nameEn: String = "",
    val nameTh: String = "",
    val startColorCode: String = "",
    val endColorCode: String = "",
    val grade: Float = 0f
) {
    val startColor: Int get() = startColorCode.toColor()
    val endColor: Int get() = endColorCode.toColor()
    val name: String get() = localize(nameEn, nameTh, nameEn)
}