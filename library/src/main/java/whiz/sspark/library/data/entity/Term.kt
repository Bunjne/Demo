package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class Term(
        @SerializedName("id")val id: String = "",
        @SerializedName("roomNumber") val roomNumber: Int? = null,
        @SerializedName("term") val term: Int = 0,
        @SerializedName("year") val year: Int = 0,
        @SerializedName("startAt") val startAt: Date = Date(),
        @SerializedName("endAt") val endAt: Date = Date(),
        @SerializedName("academicGrade") val academicGrade: Int? = null
)