package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize
import java.util.*

data class Event(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("location") val location: String = "",
    @SerializedName("venueEn") val venueEn: String = "",
    @SerializedName("venueTh") val venueTh: String = "",
    @SerializedName("descriptionEn") private val descriptionEn: String = "",
    @SerializedName("descriptionTh") private val descriptionTh: String = "",
    @SerializedName("coverImageUrl") val coverImageUrl: String = "",
    @SerializedName("createdBy") val createdBy: String = "",
    @SerializedName("organizedByEn") private val organizedByEn: String = "",
    @SerializedName("organizedByTh") private val organizedByTh: String = "",
    @SerializedName("termAndConditionEn") private val termAndConditionEn: String = "",
    @SerializedName("termAndConditionTh") private val termAndConditionTh: String = "",
    @SerializedName("publisherId") private val publisherId: String = "",
    @SerializedName("startedAt") val startedAt: Date = Date(),
    @SerializedName("endedAt") val endedAt: Date = Date(),
    @SerializedName("contacts") val _contacts: List<EventContact>? = null,
    @SerializedName("ticket") val ticket: EventTicket? = null,
    @SerializedName("publisher") val publisher: EventPublisher? = null,
    @SerializedName("ticketTypes") val ticketTypes: List<TicketType>? = null,
    @SerializedName("eventAudienceGroups") val eventAudienceGroups: List<EventAudienceGroups>? = null
) {
    val name: String get() = localize(nameEn, nameTh, nameEn, false)
    val venue: String get() = localize(venueEn, venueTh, venueEn, false)
    val description: String get() = localize(descriptionEn, descriptionTh, descriptionEn, false)
    val organizedBy: String get() = localize(organizedByEn, organizedByTh, organizedByEn, false)
    val termAndCondition: String get() = localize(termAndConditionEn, termAndConditionTh, termAndConditionEn, false)
    val contacts: List<EventContact> get() = _contacts ?: listOf()
}

data class EventTicketInformationItem(
    @SerializedName("iconUrl") val iconUrl: String = "",
    @SerializedName("title") val title: String = ""
)

data class EventAudienceGroups(
    @SerializedName("eventId") val eventId: Long = 0L,
    @SerializedName("audienceGroupId") val audienceGroupId: String? = ""
)

data class EventPublisher(
    @SerializedName("id") val id: String = "",
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("imageName") val imageName: String = "",
    @SerializedName("createdAt") val createdAt: String? = ""
)