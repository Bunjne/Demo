package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class EventRegisteredDTO (
    @SerializedName("upcoming") val upcomingEvents: List<EventListDTO> = listOf(),
    @SerializedName("history") val historicalEvents: List<HistoricalEventDTO> = listOf()
)

data class HistoricalEventDTO(
    @SerializedName("term") val term: Int = 0,
    @SerializedName("year") val year: Int = 0,
    @SerializedName("events") val events: List<EventListDTO> = listOf()
)