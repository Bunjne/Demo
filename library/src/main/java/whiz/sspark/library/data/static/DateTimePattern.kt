package whiz.sspark.library.data.static

object DateTimePattern {
    //General
    const val generalYear = "yyyy"
    const val twoPositionYear = "yy"
    const val serviceDateFullFormat = "yyyy-MM-dd'T'HH:mm:ss"

    const val dayNameThreePositionFormat = "EEE"
    const val fullDayNameFormat = "EEEE"
    const val singleDayFormat = "d"

    const val serviceDateFormat = "dd/MM/yyyy"

    const val todayAbbreviatedDateFormatEn = "d MMMM yyyy"
    const val todayAbbreviatedDayMonthFormatTh = "d MMM "

    //Class Attendance
    const val attendanceClassDateFormatEn = "MMMM d, yyyy"
    const val attendanceClassDayMonthFormatTh = "d/M/"
    val attendanceClassYearFormatTh get() = twoPositionYear
    const val attendanceClassTimeFormat = "HH:mm"
}