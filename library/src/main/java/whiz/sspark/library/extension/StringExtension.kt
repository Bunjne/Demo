package whiz.sspark.library.extension

import java.util.regex.Pattern

fun String.convertToTime(): String {
    return if (this.isBlank()) {
        ""
    } else {
        val timeStamp = this.split(":")
        if (timeStamp.size < 2) {
            ""
        } else {
            "${timeStamp[0]}:${timeStamp[1]}"
        }
    }
}

fun String.isPhoneNumber(): Boolean = run {
    if (this.first() != '0') {
        return false
    } else if (this.length == 9 && this.take(2) != "02") {
        return false
    }

    val pattern = Pattern.compile("[0-9]")
    for (number in this) {
        if (!pattern.matcher(number.toString()).matches()) {
            return false
        }
    }

    return true
}

fun String.toPhoneNumber() = StringBuilder().append(this).apply {
    if (!this@toPhoneNumber.isPhoneNumber()) {
        return this.toString()
    } else {
        if (this.take(2) != "02") {
            this.insert(3, '-')
            this.insert(7, '-')
            if (this.length > 12) {
                this.insert(12, '-')
            }
        } else  {
            this.insert(2, '-')
            this.insert(6, '-')
            if (this.length > 11) {
                this.insert(11, '-')
            }
        }
    }
}.toString()