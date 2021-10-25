package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize
import java.util.*

data class ExamScheduleDTO(
        @SerializedName("midterm") private val _midterm: List<ExamScheduleCourseDTO>? = null,
        @SerializedName("final") private val _final: List<ExamScheduleCourseDTO>? = null,
) {
        val midterm get() = _midterm ?: listOf()
        val final get() = _final ?: listOf()
}

data class ExamScheduleCourseDTO(
        @SerializedName("code") val code: String = "",
        @SerializedName("nameEn") val nameEn: String = "",
        @SerializedName("nameTh") val nameTh: String = "",
        @SerializedName("room") val room: String = "",
        @SerializedName("date") val date: Date = Date(),
        @SerializedName("startTime") val startTime: String = "",
        @SerializedName("endTime") val endTime: String = "",
) {
        val name get() = localize(nameEn, nameTh, nameEn, false)
}