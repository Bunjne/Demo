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
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewMenuContactInfoDialogBinding
import whiz.sspark.library.extension.showUserProfileCircle
import whiz.sspark.library.utility.getAlphaPercentage

class MenuContactInfoDialogView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding: ViewMenuContactInfoDialogBinding by lazy {
        ViewMenuContactInfoDialogBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(contactInfo: MenuContactInfoItem,
             onContactClicked: (String) -> Unit,
             onCloseClicked: () -> Unit) {

        with(binding) {
            ivProfileImage.showUserProfileCircle(contactInfo.imageUrl, getGender(contactInfo.gender).type)
            tvName.text = contactInfo.name
            tvDescription.text = contactInfo.description

            with(rvContacts) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = MenuContactInfoAdapter(
                    context = context,
                    menuContactItems = contactInfo.contactInfoItems,
                    onContactClicked = onContactClicked
                )

                val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                divider.setDrawable(
                    ContextCompat.getDrawable(context, R.drawable.divider_base_dialog_item)!!.apply {
                        alpha = getAlphaPercentage(80)
                    }
                )
                addItemDecoration(divider)
            }

            tvClose.setOnClickListener {
                onCloseClicked()
            }
        }
    }
}