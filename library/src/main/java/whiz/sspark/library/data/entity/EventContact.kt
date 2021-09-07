package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class EventContact(
        @SerializedName("eventId") val eventId: Long = 0,
        @SerializedName("key")val key: String = "",
        @SerializedName("value")val value: String = "",
        @SerializedName("iconUrl")val iconUrl: String = ""
)