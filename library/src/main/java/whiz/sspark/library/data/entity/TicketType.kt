package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize
import java.util.*

data class TicketType(
        @SerializedName("id") val id: Long = 0L,
        @SerializedName("eventId") val eventId: Long = 0L,
        @SerializedName("code") val code: String? = "",
        @SerializedName("nameTh") val _nameTh: String = "",
        @SerializedName("nameEn") val _nameEn: String = "",
        @SerializedName("descriptionEn") val _descriptionEn: String = "",
        @SerializedName("descriptionTh") val _descriptionTh: String = "",
        @SerializedName("price") val price: Double = 0.0,
        @SerializedName("quantityAvailable") val quantityAvailable: Int?,
        @SerializedName("quantityUsed") val quantityUsed: Int = 0,
        @SerializedName("quantityLimit") val quantityLimit: Int = 0,
        @SerializedName("validUntil") val validUntil: Date = Calendar.getInstance().time,
        @SerializedName("createdAt") val createdAt: Date = Calendar.getInstance().time,
        @SerializedName("updatedAt") val updatedAt: Date = Calendar.getInstance().time
) {
        val name: String get() = localize(_nameEn, _nameTh, _nameEn, false)
        val description: String get() = localize(_descriptionEn, _descriptionTh, _descriptionEn, false)
}

data class EventManagementTicketType(
        @SerializedName("id") val id: Long = 0,
        @SerializedName("name") val name: String = "",
        @SerializedName("description") val description: String = "",
        @SerializedName("price") val price: Double = 0.0,
        @SerializedName("quantity") val quantity: Long = 0,
        @SerializedName("validUntil") val validUntil: Date = Date()
)
