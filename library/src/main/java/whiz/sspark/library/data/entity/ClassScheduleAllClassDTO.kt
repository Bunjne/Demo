package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize
import java.util.*

data class ClassScheduleAllClassDTO(
    @SerializedName("code") val code: String,
    @SerializedName("sectionNumber") val sectionNumber: String,
    @SerializedName("nameEn") val nameEn: String,
    @SerializedName("nameTh") val nameTh: String,
    @SerializedName("startAt") val startAt: Date,
    @SerializedName("endAt") val endAt: Date,
) {
    val name get() = localize(nameEn, nameTh, nameEn, false)
}