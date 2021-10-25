package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class AddCommentAPIBody (
    @SerializedName("message") val message: String
)