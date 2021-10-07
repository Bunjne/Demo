package whiz.sspark.library.data.entity

import whiz.sspark.library.view.general.information_dialog.InformationDialogAdapter

data class SeniorOutcomeIndex(
    val level: String = "",
    val description: String = ""
)

fun List<SeniorOutcomeIndex>.toInformationItems(): List<InformationDialogAdapter.Item> {
    return this.map { InformationDialogAdapter.Item(seniorOutcomeIndex = it) }
}
