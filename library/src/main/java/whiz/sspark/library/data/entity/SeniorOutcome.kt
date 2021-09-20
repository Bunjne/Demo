package whiz.sspark.library.data.entity

import whiz.sspark.library.view.general.information_dialog.InformationDialogAdapter

data class SeniorOutcome(
    val level: String = "",
    val description: String = ""
)

fun List<SeniorOutcome>.toInformationItems(): List<InformationDialogAdapter.Item> {
    return this.map { InformationDialogAdapter.Item(seniorOutcome = it) }
}
