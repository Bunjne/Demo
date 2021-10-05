package whiz.tss.sspark.s_spark_android.data.enum

enum class GrantType(val type: String) {
    LOGIN("password"),
    REFRESH_TOKEN("refresh_token")
}