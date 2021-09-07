package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class MenuItem(
    @SerializedName("code") val code: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("imageUrl") val imageUrl: String = ""
    )
