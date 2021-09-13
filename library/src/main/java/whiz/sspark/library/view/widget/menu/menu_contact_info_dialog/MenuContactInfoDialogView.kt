package whiz.sspark.library.view.widget.menu.menu_contact_info_dialog

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.MenuContactInfoItem
import whiz.sspark.library.databinding.ViewMenuContactInfoDialogBinding
import whiz.sspark.library.extension.getPercentage
import whiz.sspark.library.extension.show

class MenuContactInfoDialogView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding: ViewMenuContactInfoDialogBinding by lazy {
        ViewMenuContactInfoDialogBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val menuContactItems = mutableListOf<MenuContactInfoAdapter.MenuContactItem>()

    fun init(contactInfo: MenuContactInfoItem,
             onContactClicked: (String) -> Unit) {

        with(binding) {

            ivProfileImage.show(contactInfo.imageUrl)
            tvClose.text = resources.getString(R.string.menu_contact_info_close_text)
            tvName.text = contactInfo.name
            tvDescription.text = contactInfo.description

            menuContactItems.addAll(transformToItems(contactInfo))
            with(rvContacts) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = MenuContactInfoAdapter(
                    context = context,
                    menuContactItems = menuContactItems,
                    onContactClicked = onContactClicked
                )
            }

            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(
                ContextCompat.getDrawable(context, R.drawable.divider_base_dialog_item)!!.apply {
                    alpha = 255.getPercentage(80).toInt()
                }
            )
            rvContacts.addItemDecoration(divider)
        }
    }

    private fun transformToItems(menuContact: MenuContactInfoItem): MutableList<MenuContactInfoAdapter.MenuContactItem> {
        val items = mutableListOf<MenuContactInfoAdapter.MenuContactItem>()
        val contacts = listOf(
            menuContact.personalPhone,
            menuContact.officePhone,
            menuContact.personalEmail,
            menuContact.officeEmail
        )
        contacts.forEachIndexed { index, contact ->
            when(index) {
                0 -> {
                    val hasPersonalPhone = contact.isNotEmpty()
                    if(hasPersonalPhone) {
                        items.add(MenuContactInfoAdapter.MenuContactItem(R.drawable.ic_phone, contact, context.resources.getString(R.string.menu_contact_info_personal_phone_text)))
                    }
                }
                1 -> {
                    val hasOfficePhone = contact.isNotEmpty()
                    if(hasOfficePhone) {
                        items.add(MenuContactInfoAdapter.MenuContactItem(R.drawable.ic_phone, contact, context.resources.getString(R.string.menu_contact_info_office_phone_text)))
                    }
                }
                2 -> {
                    val hasPersonalEmail = contact.isNotEmpty()
                    if(hasPersonalEmail) {
                        items.add(MenuContactInfoAdapter.MenuContactItem(R.drawable.ic_at_sign, contact,context.resources.getString(R.string.menu_contact_info_personal_email_text)))
                    }
                }
                else -> {
                    val hasOfficeEmail = contact.isNotEmpty()
                    if(hasOfficeEmail) {
                        items.add(MenuContactInfoAdapter.MenuContactItem(R.drawable.ic_at_sign, contact, context.resources.getString(R.string.menu_contact_info_office_email_text)))
                    }
                }
            }
        }

        return items
    }

    fun setOnCloseClickListener(onCloseClicked: () -> Unit) {
        binding.tvClose.setOnClickListener {
            onCloseClicked()
        }
    }
}