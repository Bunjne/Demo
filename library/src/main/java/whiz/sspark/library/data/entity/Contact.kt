package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class Contact(
    @SerializedName("Id") val id: String = "",
    @SerializedName("iconImageUrl") val iconImageUrl: String = "",
    @SerializedName("name") val name: String = ""
)
