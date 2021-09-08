package whiz.sspark.library.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class ContactInfo(
    @SerializedName("keyEn") val keyEn: String = "",
    @SerializedName("keyTh") val keyTh: String = "",
    @SerializedName("displayTitle") val displayTitle: String = "",
    @SerializedName("value") val value: String = "",
    @SerializedName("type") val type: String = ""
) {
    val contactTitle: String get() = localize(keyEn, keyTh, keyEn)
}
