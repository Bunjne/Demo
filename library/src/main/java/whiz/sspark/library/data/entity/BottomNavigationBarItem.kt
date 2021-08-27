package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.data.enum.BottomNavigationType

data class BottomNavigationBarItem(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("title") val title: String = "",
        @SerializedName("type") val type: String = BottomNavigationType.ICON.id,
        @SerializedName("imageUrl") val imageUrl: String = "",
        @SerializedName("gender") val gender: Long = 0,
        @SerializedName("imageResource") val imageResource: Int? = null,
        @SerializedName("colors") val colors: List<Int> = listOf()
)