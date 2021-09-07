package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize
import java.util.*

data class EventTicket(
        @SerializedName("eventId") val eventId: Long = 0,
        @SerializedName("ticketTypeId") val ticketTypeId: Long = 0,
        @SerializedName("userId") val userId: String = "",
        @SerializedName("number") val number: String? = null,
        @SerializedName("sequence") val sequence: Int? = null,
        @SerializedName("qrCode") val qrCode: String = "",
        @SerializedName("isUsed") val isUsed: Boolean = false,
        @SerializedName("purchasedAt") val purchasedAt: Date = Date(),
        @SerializedName("redeemedAt") val redeemedAt: Date? = null,
        @SerializedName("ticketType") val ticketType: TicketType? = null,
        @SerializedName("attendeeInfo") val attendeeInfo: Student = Student(),
        @SerializedName("remarkEn") val remarkEn: String? = null,
        @SerializedName("remarkTh") val remarkTh: String? = null
) {
        val remark: String get() = localize(remarkEn ?: "", remarkTh ?: "", remarkEn ?: "", false)
}

data class EventTicketType(
        @SerializedName("id") val id: Long = 0,
        @SerializedName("eventId") val eventId: Long = 0,
        @SerializedName("code") val code: String = "",
        @SerializedName("nameEn") private val nameEn: String = "",
        @SerializedName("nameTh") private val nameTh: String = "",
        @SerializedName("descriptionEn") private val descriptionEn: String = "",
        @SerializedName("descriptionTh") private val descriptionTh: String = "",
        @SerializedName("quantityAvailable") val quantityAvailable: Int = 0,
        @SerializedName("quantityUsed") val quantityUsed: Int = 0,
        @SerializedName("quantityLimit") val quantityLimit: Int = 0,
        @SerializedName("price") val price: Double = 0.0,
        @SerializedName("validUntil") val validUntil: Date? = null
) {
    val name: String get() = localize(nameEn, nameTh, nameEn)
    val description: String get() = localize(descriptionEn, descriptionTh, descriptionEn)
}