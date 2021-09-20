package whiz.sspark.library.data.entity

import android.content.Context
import whiz.sspark.library.R
import whiz.sspark.library.view.widget.menu.menu_contact_info_dialog.MenuContactInfoAdapter

data class MenuContactInfoItem(
    val imageUrl: String,
    val gender: String,
    val name: String,
    val description: String,
    val officePhone: String = "",
    val personalPhone: String = "",
    val officeEmail: String = "",
    val personalEmail: String = ""
)

fun MenuContactInfoItem.toMenuContactInfoItems(context: Context): List<MenuContactInfoAdapter.MenuContactItem> {
    val items = mutableListOf<MenuContactInfoAdapter.MenuContactItem>()
    val contacts = listOf(
        this.personalPhone,
        this.officePhone,
        this.personalEmail,
        this.officeEmail
    )
    contacts.forEachIndexed { index, contact ->
        when(index) {
            0 -> {
                val hasPersonalPhone = contact.isNotEmpty()
                if(hasPersonalPhone) {
                    items.add(
                        MenuContactInfoAdapter.MenuContactItem(
                            contactIconRes = R.drawable.ic_phone,
                            contact = contact,
                            contactDescription = context.resources.getString(R.string.menu_contact_info_personal_phone_text)
                        )
                    )
                }
            }
            1 -> {
                val hasOfficePhone = contact.isNotEmpty()
                if(hasOfficePhone) {
                    items.add(
                        MenuContactInfoAdapter.MenuContactItem(
                            contactIconRes = R.drawable.ic_phone,
                            contact = contact,
                            contactDescription = context.resources.getString(R.string.menu_contact_info_office_phone_text)
                        )
                    )
                }
            }
            2 -> {
                val hasPersonalEmail = contact.isNotEmpty()
                if(hasPersonalEmail) {
                    items.add(
                        MenuContactInfoAdapter.MenuContactItem(
                            contactIconRes = R.drawable.ic_at_sign,
                            contact = contact,
                            contactDescription = context.resources.getString(R.string.menu_contact_info_personal_email_text)
                        )
                    )
                }
            }
            else -> {
                val hasOfficeEmail = contact.isNotEmpty()
                if(hasOfficeEmail) {
                    items.add(
                        MenuContactInfoAdapter.MenuContactItem(
                            contactIconRes = R.drawable.ic_at_sign,
                            contact = contact,
                            contactDescription = context.resources.getString(R.string.menu_contact_info_office_email_text)
                        )
                    )
                }
            }
        }
    }

    return items
}