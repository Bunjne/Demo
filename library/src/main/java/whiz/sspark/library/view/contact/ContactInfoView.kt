package whiz.sspark.library.view.contact

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.data.entity.ContactInfo
import whiz.sspark.library.databinding.ViewContactInfoBinding
import whiz.sspark.library.extension.show

class ContactInfoView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewContactInfoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(contact: Contact,
             onContactInfoClicked: (ContactInfo) -> Unit,
             onClosed: () -> Unit) {
        with(contact) {
            binding.tvContactName.text = title
            binding.ivProfile.show(imageUrl)
            binding.tvClose.setOnClickListener {
                onClosed()
            }

            binding.llContactInfo.removeAllViews()
            contacts.forEach {
                binding.llContactInfo.addView(ContactInfoItemView(context).apply {
                    init(it, onContactInfoClicked)
                })

                binding.llContactInfo.addView(ContactInfoItemView(context).apply {
                    init(it, onContactInfoClicked)
                })
            }
        }
    }
}