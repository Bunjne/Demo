package whiz.sspark.library.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import whiz.sspark.library.utility.localize

@Parcelize
data class ClassSharedSection(
        @SerializedName("sectionId") val sectionId: Long = 0L,
        @SerializedName("courseCode") val courseCode: String = "",
        @SerializedName("courseNameEn") val _courseNameEn: String = "",
        @SerializedName("courseNameTh") val _courseNameTh: String = "",
        @SerializedName("sectionNumber") val sectionNumber: String = ""
) : Parcelable {
        val courseName: String get() = localize(_courseNameEn, _courseNameTh, _courseNameEn)
}