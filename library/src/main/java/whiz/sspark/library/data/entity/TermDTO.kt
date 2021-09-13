package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class TermDTO(
        @SerializedName("id")val id: String = "",
        @SerializedName("term") val term: Long = 0,
        @SerializedName("year") val year: Long = 0,
        @SerializedName("academicGrade") val academicGrade: Int = 0
)