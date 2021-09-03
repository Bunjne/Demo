package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class LearningOutcomeAPIBody(
    @SerializedName("semesterId") val semesterId: Int = 1
)