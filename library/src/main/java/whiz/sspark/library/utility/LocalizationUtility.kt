package whiz.sspark.library.utility

import java.lang.StringBuilder
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

fun convertToFullName(firstName: String?, middleName: String?, lastName: String?): String {
    val fullName = StringBuilder()

    if (!firstName.isNullOrBlank()) {
        fullName.append(firstName)
        fullName.append(" ")
    }

    if (!middleName.isNullOrBlank()) {
        fullName.append(middleName)
        fullName.append(" ")
    }

    if (!lastName.isNullOrBlank()) {
        fullName.append(lastName)
        fullName.append(" ")
    }

    return fullName.toString()
}

fun convertToFullNameWithPosition(position: String?, firstName: String?, middleName: String?, lastName: String?): String {
    val fullName = StringBuilder()

    if (!position.isNullOrBlank()) {
        fullName.append(firstName)
        fullName.append(" ")
    }

    if (!firstName.isNullOrBlank()) {
        fullName.append(firstName)
        fullName.append(" ")
    }

    if (!middleName.isNullOrBlank()) {
        fullName.append(middleName)
        fullName.append(" ")
    }

    if (!lastName.isNullOrBlank()) {
        fullName.append(lastName)
        fullName.append(" ")
    }

    return fullName.toString()
}