package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class EventRegisteredDTO (
    @SerializedName("upcoming") val upcomingEvents: List<EventListDTO> = listOf(),
    @SerializedName("history") val historicalEvents: List<EventListDTO> = listOf()
)