package whiz.sspark.library.utility

import java.util.*

private val defaultEmptyValueEn = "N/A"
private val defaultEmptyValueTh = "ไม่พบข้อมูล"
private val defaultEmptyValueCn = "无数据"

private val defaultPositionValueEn = "AJARN"
private val defaultPositionValueTh = "อาจารย์"
private val defaultPositionValueCn = "AJARN"

fun localize(valueEn: String, valueTh: String, valueCn:String, isDefaultTextEnabled: Boolean = true): String {
    val localeLanguage = Locale.getDefault().language
    val desiredValue = when (localeLanguage) {
        "th" -> valueTh
        "en" -> valueEn
        else -> valueCn
    }

    return if(isDefaultTextEnabled) {
        if (desiredValue.isBlank()) {
            when (localeLanguage) {
                "th" -> defaultEmptyValueTh
                "en" -> defaultEmptyValueEn
                else -> defaultEmptyValueCn
            }
        } else {
            desiredValue
        }
    } else {
        desiredValue
    }
}

fun isThaiLanguage() = Locale.getDefault().language == "th"
fun isChineseLanguage() = Locale.getDefault().language == "zh"