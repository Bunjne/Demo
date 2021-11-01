package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class Attendance(
        @SerializedName("summary") val summary: AttendanceSummary = AttendanceSummary(),
        @SerializedName("attendance") private val _attendanceDetails: List<AttendanceDetail>? = listOf(),
        @SerializedName("sessions") private val _sessions: List<AttendanceDetail>? = listOf()
) {
        val sessions: List<AttendanceDetail> get() = _sessions ?: listOf()
        val attendanceDetails: List<AttendanceDetail> get() = _attendanceDetails ?: listOf()
}

data class AttendanceSummary(
        @SerializedName("totalAttendance") val totalAttendance: Int = 0,
        @SerializedName("checkCount") val checkCount: Double = 0.0,
        @SerializedName("leaveCount") val leaveCount: Double = 0.0,
        @SerializedName("lateCount") val lateCount: Double = 0.0,
        @SerializedName("absentCount") val absentCount: Double = 0.0,
)

data class AttendanceDetail(
        @SerializedName("id") val id: String = "",
        @SerializedName("index") val index: Int = 0,
        @SerializedName("status") val status: String = "",
        @SerializedName("dateTime") val dateTime: Date = Date(),
        @SerializedName("checkCount") val checkCount: Double = 0.0,
        @SerializedName("leaveCount") val leaveCount: Double = 0.0,
        @SerializedName("lateCount") val lateCount: Double = 0.0,
        @SerializedName("absentCount") val absentCount: Double = 0.0,
)