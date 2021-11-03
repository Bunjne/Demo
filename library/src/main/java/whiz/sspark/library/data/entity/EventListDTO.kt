package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class EventListDTO(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("startAt") val startAt: Date,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("location") val location: String
) {
    fun convertToEventListItem() = EventList(
        id = id,
        name = name,
        startAt = startAt,
        imageUrl = imageUrl,
        location = location
    )
}