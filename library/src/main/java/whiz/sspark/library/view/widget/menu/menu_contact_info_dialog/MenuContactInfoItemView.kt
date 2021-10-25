package whiz.sspark.library.view.widget.menu.menu_contact_info_dialog

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewMenuContactInfoItemBinding
import whiz.sspark.library.extension.show

class MenuContactInfoItemView: FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding: ViewMenuContactInfoItemBinding by lazy {
        ViewMenuContactInfoItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(contactIconRes: Int,
             contact: String,
             contactDescription: String,
             onContactClicked: () -> Unit) {

        with(binding) {
            tvContactInfoHeader.text = contact
            tvContactDescription.text = contactDescription
            ivContactIcon.show(contactIconRes)
            ivEndIcon.show(R.drawable.ic_arrow_right)
            root.setOnClickListener {
                onContactClicked()
            }
        }
    }
}