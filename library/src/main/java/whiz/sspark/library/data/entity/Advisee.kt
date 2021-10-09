package whiz.sspark.library.data.entity

data class Advisee(
    val nickname: String,
    val code: String,
    val name: String,
    val imageUrl: String,
    val gender: String,
    val gpa: Float,
    val credit: Int,
    val totalCredit: Int
)