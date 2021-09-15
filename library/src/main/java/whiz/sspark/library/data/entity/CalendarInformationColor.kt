package whiz.sspark.library.data.entity

import whiz.sspark.library.extension.toColor
import whiz.sspark.library.view.general.information_dialog.InformationDialogAdapter

data class CalendarInformationColor(
    val _color: String = "",
    val description: String = ""
) {
    val color get() = _color.toColor()
    companion object {
        fun getCalendarInfoColors(): List<CalendarInformationColor> = listOf(
            CalendarInformationColor("#FFBF46", "วันเปิด-ปิด ภาคการศึกษา"),
            CalendarInformationColor("#EAEAEA", "กิจกรรมทั่วไป"),
            CalendarInformationColor("#DC3030", "วันสอบ"),
            CalendarInformationColor("#0DC45A", "วันหยุด")
        )
    }
}

fun List<CalendarInformationColor>.toInformationItems(): List<InformationDialogAdapter.Item> {
    return this.map { InformationDialogAdapter.Item(calendarInformationColor = it) }
}