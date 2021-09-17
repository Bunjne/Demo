package whiz.sspark.library.data.entity

import whiz.sspark.library.extension.toColor
import whiz.sspark.library.view.general.information_dialog.InformationDialogAdapter

data class CalendarInformationIndex(
    val _color: String = "",
    val description: String = ""
) {
    val color get() = _color.toColor()
    companion object {
        fun getCalendarInformationIndexes(): List<CalendarInformationIndex> = listOf(
            CalendarInformationIndex("#FFBF46", "วันเปิด-ปิด ภาคการศึกษา"),
            CalendarInformationIndex("#EAEAEA", "กิจกรรมทั่วไป"),
            CalendarInformationIndex("#DC3030", "วันสอบ"),
            CalendarInformationIndex("#0DC45A", "วันหยุด")
        )
    }
}

fun List<CalendarInformationIndex>.toInformationItems(): List<InformationDialogAdapter.Item> {
    return this.map { InformationDialogAdapter.Item(calendarInformationIndex = it) }
}