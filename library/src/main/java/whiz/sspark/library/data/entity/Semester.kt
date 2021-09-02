package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class Semester(
        @SerializedName("id") val id: Long = 0,
        @SerializedName("academicYear") val academicYear: Int = 0,
        @SerializedName("term") val term: Int = 0,
        @SerializedName("startedAt") val startedAt: Date = Date(),
        @SerializedName("endedAt") val endedAt: Date = Date(),
)