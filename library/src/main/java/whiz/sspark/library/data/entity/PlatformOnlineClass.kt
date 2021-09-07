package whiz.sspark.library.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import whiz.sspark.library.utility.localize

@Parcelize
data class PlatformOnlineClass (
    @SerializedName("id") val onlineClassId: String = "",
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("displayName") val displayName: String? = "",
    @SerializedName("logoUrl") val logoUrl: String? = "",
    @SerializedName("url") val url: String? = "",
    @SerializedName("isGeneratedUrl") val isGeneratedUrl: Boolean = false,
    @SerializedName("isShown") val isShown: Boolean = false
) : Parcelable {
    val name: String get() = localize(nameEn, nameTh, nameEn, false)
}