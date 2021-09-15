package whiz.sspark.library.data.entity

import whiz.sspark.library.view.general.information_dialog.InformationDialogAdapter

data class JuniorOutcome(
    val number: Int = 0,
    val description: String = ""
) {
    companion object {
        fun getJuniorOutComes(): List<JuniorOutcome> = listOf(
            JuniorOutcome(0, "ไม่สามารถประเมินได้"),
            JuniorOutcome(1, "จำเป็นต้องได้รับการพัฒนาอย่างยิ่ง"),
            JuniorOutcome(2, "กำลังพัฒนา"),
            JuniorOutcome(3, "อยู่ในระดับที่ทำได้"),
            JuniorOutcome(4, "อยู่ในระดับที่ทำได้ดี"),
            JuniorOutcome(5, "อยู่ในระดับที่ทำได้ดีมาก"),
        )
    }
}

fun List<JuniorOutcome>.toInformationItems(): List<InformationDialogAdapter.Item> {
    return this.map { InformationDialogAdapter.Item(juniorOutcome = it) }
}
