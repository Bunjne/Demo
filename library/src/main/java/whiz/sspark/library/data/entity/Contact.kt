package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class Contact(
    @SerializedName("imageUrl") val imageUrl: String = "",
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("contacts") val contacts: List<ContactInfo> = listOf()
) {
    val title: String get() = localize(nameEn, nameTh, nameEn)
}
