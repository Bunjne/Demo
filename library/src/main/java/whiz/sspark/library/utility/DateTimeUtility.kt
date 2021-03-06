package whiz.sspark.library.utility

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import whiz.sspark.library.R
import java.text.SimpleDateFormat
import java.util.*

fun Date.toThaiYear() = Calendar.getInstance().apply {
    time = this@toThaiYear
    add(Calendar.YEAR, 543)
}.time

fun getDifferenceDayTimelineValue(sourceDate: Date, targetDate: Date): Int {
    val targetDayNumber = Calendar.getInstance().apply {
        time = targetDate
    }.get(Calendar.DAY_OF_YEAR)

    val sourceDayNumber = Calendar.getInstance().apply {
        time = sourceDate
    }.get(Calendar.DAY_OF_YEAR)

    return sourceDayNumber - targetDayNumber
}

fun getLatestUpdatedDateTime(context: Context, date: Date): String {
    val dateTimeFormat = if (isThaiLanguage()) {
        SimpleDateFormat("d/M/yy HH:mm", Locale.getDefault()).format(date.toThaiYear())
    } else {
        SimpleDateFormat("d/M/yy HH:mm", Locale.getDefault()).format(date)
    }
    return context.resources.getString(R.string.screen_header_subtitle, dateTimeFormat)
}

fun convertToLocalizeYear(year: Int): String {
    return if (isThaiLanguage()) {
        (year + 543).toString()
    } else {
        year.toString()
    }
}

fun showDatePicker(context: Context, currentDate: Calendar, minimumDate: Calendar? = null, onDateSelected: (Int, Int, Int) -> Unit) {
    DatePickerDialog(
        context,
        { _, year, monthOfYear, dayOfMonth ->
            onDateSelected(year, monthOfYear, dayOfMonth)
        },
        currentDate.get(Calendar.YEAR),
        currentDate.get(Calendar.MONTH),
        currentDate.get(Calendar.DATE)
    ).apply {
        minimumDate?.time?.time?.let {
            datePicker.minDate = it
        }
        show()
    }
}

fun showTimePicker(context: Context, currentDate: Calendar, onTimeSelected: (Int, Int) -> Unit) {
    TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            onTimeSelected(hourOfDay, minute)
        },
        currentDate.get(Calendar.HOUR_OF_DAY),
        currentDate.get(Calendar.MINUTE),
        true
    ).show()
}