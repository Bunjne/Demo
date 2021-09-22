package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class Term(
        @SerializedName("id")val id: String = "",
        @SerializedName("room") val room: Int? = null,
        @SerializedName("term") val term: Long = 0,
        @SerializedName("year") val year: Long = 0,
        @SerializedName("startDate") val startDate: Date = Date(),
        @SerializedName("fromDate") val fromDate: Date = Date(),
        @SerializedName("academicGrade") val academicGrade: Int? = null
)