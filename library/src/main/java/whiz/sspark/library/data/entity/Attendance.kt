package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.data.enum.ClassAttendanceMethod
import whiz.sspark.library.data.enum.ClassAttendanceStatus
import whiz.sspark.library.utility.localize
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

data class ClassAttendanceMemberResponse(
        @SerializedName("today") val today: Date = Date(),
        @SerializedName("students") val _students: List<ClassAttendanceMember>? = listOf()
) {
        val students: List<ClassAttendanceMember> get() = _students ?: listOf()
}

data class ClassAttendanceMember(
        @SerializedName("id") val id: Long = 0L,
        @SerializedName("code") val code: String = "",
        @SerializedName("firstNameEn") val firstNameEn: String = "",
        @SerializedName("firstNameTh") val firstNameTh: String = "",
        @SerializedName("lastNameEn") val lastNameEn: String = "",
        @SerializedName("lastNameTh") val lastNameTh: String = "",
        @SerializedName("profileImageUrl") val profileImageUrl: String = "",
        @SerializedName("status") var status: String = "",
        @SerializedName("method") var method: String = "",
        @SerializedName("colorCode") val colorCode: String? = null,
        @SerializedName("presentCount") val presentCount: Int = 0,
        @SerializedName("lateCount") val lateCount: Int = 0,
        @SerializedName("absentCount") val absentCount: Int = 0
) {
        val firstName get() = localize(firstNameEn, firstNameTh, firstNameEn, false)
        val lastName get() = localize(lastNameEn, lastNameTh, lastNameEn, false)

        fun toAttendanceClassMemberService() = AttendanceClassMemberService(
                studentId = id,
                status = if (status.isBlank()) ClassAttendanceStatus.ABSENT.status else status,
                method = if (method.isBlank()) ClassAttendanceMethod.MANUAL.method else method
        )
}

data class AttendanceClassMemberService(
        @SerializedName("studentId") val studentId: Long = 0L,
        @SerializedName("status") val status: String = "",
        @SerializedName("method") val method: String = ""
)