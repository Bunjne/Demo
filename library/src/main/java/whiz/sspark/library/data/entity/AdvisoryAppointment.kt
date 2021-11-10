package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class AdvisoryAppointment(
    @SerializedName("date") val date: Date = Date(),
    @SerializedName("slots") private val _slots: List<AdvisorySlot>? = null
) {
    val slot get() = _slots ?: listOf()
}