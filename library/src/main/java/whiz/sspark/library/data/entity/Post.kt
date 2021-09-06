package whiz.sspark.library.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Post(
        @SerializedName("id") val _id: String = "",
        @SerializedName("author") val author: ClassMember = ClassMember(),
        @SerializedName("attachments") private val _attachments: String? = null,
        @SerializedName("createdAt") val createdAt: Date = Date(),
        @SerializedName("isRead") var isRead: Boolean = false,
        @SerializedName("isLike") var isLike: Boolean = false,
        @SerializedName("message") val message: String = "",
        @SerializedName("updatedAt") val updatedAt: Date = Date(),
        @SerializedName("readsCount") var readCount: Int = 0,
        @SerializedName("likesCount") var likeCount: Int = 0,
        @SerializedName("commentsCount") var commentCount: Int = 0
) : Parcelable {
    val id: Any get() = _id.toLongOrNull() ?: _id
    val attachments: String get() = _attachments ?: ""
}