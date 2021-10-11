package whiz.sspark.library.data.static

object DateTimePattern {
    //General
    const val generalYear = "yyyy"
    const val twoPositionYear = "yy"
    const val generalTime = "HH:mm"
    const val generalShortTime = "H:mm"
    const val serviceDateFullFormat = "yyyy-MM-dd'T'HH:mm:ss"
    const val serviceDateFormat = "dd/MM/yyyy"

    const val dayNameThreePositionFormat = "EEE"
    const val fullDayNameFormat = "EEEE"
    const val fullDayNameWithDayFormat = "EEEE d"
    const val singleDayFormat = "d"

    const val dayFullMonthFormatTh = "d MMMM "

    const val fullMonthFormatTh = "MMMM "
    const val fullMonthYearFormat = "MMMM yyyy"

    const val dayAbbreviateMonthFormatEn = "d MMM yyyy"
    const val dayAbbreviateMonthFormatTh = "d MMM "

    const val todayAbbreviatedDateFormatEn = "d MMMM yyyy"
    const val todayAbbreviatedDayMonthFormatTh = "d MMM "

    const val shortDayAndMonthFormatEn = "d/M/yy"
    const val shortDayAndMonthFormatTh = "d/M/"
    const val shortDayAndMonthNoYearFormatTh = "d/M"

    const val monthYearFormat = "MM/yyyy"

    //Class Attendance
    const val attendanceClassDateFormatEn = "MMMM d, yyyy"
    val attendanceClassDayMonthFormatTh get() = shortDayAndMonthFormatTh
    val attendanceClassYearFormatTh get() = twoPositionYear
    const val attendanceClassTimeFormat = "HH:mm"

    //ClassGroup
    val classGroupDayMonthFormatTh get() = dayFullMonthFormatTh

    //Class schedule
    const val classScheduleServiceDateFormat = "yyyy-MM-dd"
}