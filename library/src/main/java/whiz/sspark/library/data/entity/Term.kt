package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class Term(
    @SerializedName("id")val id: String = "",
    @SerializedName("room") val room: Long? = null,
    @SerializedName("term") val term: Long = 0,
    @SerializedName("year") val year: Long = 0,
    @SerializedName("academicGrade") val academicGrade: Int? = null
)