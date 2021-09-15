package whiz.sspark.library.data.entity

import whiz.sspark.library.view.general.information_dialog.InformationDialogAdapter

data class SeniorOutcome(
    val level: String = "",
    val description: String = ""
) {
    companion object {
        fun getSeniorOutComes(): List<SeniorOutcome> = listOf(
            SeniorOutcome("Beginning", "เริ่มต้น"),
            SeniorOutcome("Developing", "กำลังพัฒนา"),
            SeniorOutcome("Proficient", "อยู่ในระดับที่ทำได้ดี"),
            SeniorOutcome("Advanced", "อยู่ในระดับที่ทำได้ดีมาก"),
        )
    }
}

fun List<SeniorOutcome>.toInformationItems(): List<InformationDialogAdapter.Item> {
    return this.map { InformationDialogAdapter.Item(seniorOutcome = it) }
}
