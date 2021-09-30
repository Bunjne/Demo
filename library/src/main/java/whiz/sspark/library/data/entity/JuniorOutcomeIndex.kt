package whiz.sspark.library.data.entity

import whiz.sspark.library.view.general.information_dialog.InformationDialogAdapter

data class JuniorOutcomeIndex(
    val number: Int = 0,
    val description: String = ""
)

fun List<JuniorOutcomeIndex>.toInformationItems(): List<InformationDialogAdapter.Item> {
    return this.map { InformationDialogAdapter.Item(juniorOutcomeIndex = it) }
}
