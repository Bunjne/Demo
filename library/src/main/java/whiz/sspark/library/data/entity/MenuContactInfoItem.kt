package whiz.sspark.library.data.entity

import whiz.sspark.library.view.widget.menu.menu_contact_info_dialog.MenuContactInfoAdapter

data class MenuContactInfoItem(
    val imageUrl: String,
    val gender: String,
    val name: String,
    val description: String,
    val officePhone: String = "",
    val personalPhone: String = "",
    val officeEmail: String = "",
    val personalEmail: String = "",
    val contactInfoItems: List<MenuContactInfoAdapter.MenuContactItem> = listOf()
)