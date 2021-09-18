package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class Attendance(
        @SerializedName("id") val id: Long = 0L,
        @SerializedName("validClassCount") val validClassCount: Int = 0,
        @SerializedName("pastClassCount") val pastClassCount: Int = 0,
        @SerializedName("presentPercent") val presentPercent: Double = 0.0,
        @SerializedName("leavePercent") val leavePercent: Double = 0.0,
        @SerializedName("latePercent") val latePercent: Double = 0.0,
        @SerializedName("absentPercent") val absentPercent: Double = 0.0,
        @SerializedName("classes") private val _classes: List<ClassAttendance>? = listOf()
) {
    val classes: List<ClassAttendance> get() = _classes ?: listOf()
}

data class ClassAttendance(
        @SerializedName("id") val _id: String = "",
        @SerializedName("name") val name: String = "",
        @SerializedName("createdAt") val createdAt: Date = Date(),
        @SerializedName("startTime") val startTime: String = "",
        @SerializedName("endTime") val endTime: String = "",
        @SerializedName("checkedAt") val checkedAt: Date = Date(),
        @SerializedName("status") val status: String = "",
        @SerializedName("presentCount") val presentCount: Int = 0,
        @SerializedName("absentCount") val absentCount: Int = 0,
        @SerializedName("lateCount") val lateCount: Int = 0
) {
        val id get() = _id.toLongOrNull() ?: _id
}