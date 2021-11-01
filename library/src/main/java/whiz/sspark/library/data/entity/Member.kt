package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class Member(
        @SerializedName("instructors") val instructors: List<ClassMember> = listOf(),
        @SerializedName("students") val students: List<ClassMember> = listOf(),
        @SerializedName("guardians") val guardians: List<ClassMember> = listOf()
)