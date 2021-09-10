package whiz.sspark.library.data.entity

import whiz.sspark.library.extension.toColor

data class GradeSummary (
    val name: String = "",
    val startColorCode: String = "",
    val endColorCode: String = "",
    val grade: Float = 0f
) {
    val startColor: Int get() = startColorCode.toColor()
    val endColor: Int get() = endColorCode.toColor()
}