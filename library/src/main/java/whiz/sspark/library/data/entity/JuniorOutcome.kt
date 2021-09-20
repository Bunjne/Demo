package whiz.sspark.library.data.entity

import whiz.sspark.library.view.general.information_dialog.InformationDialogAdapter

data class JuniorOutcome(
    val number: Int = 0,
    val description: String = ""
)

fun List<JuniorOutcome>.toInformationItems(): List<InformationDialogAdapter.Item> {
    return this.map { InformationDialogAdapter.Item(juniorOutcome = it) }
}
