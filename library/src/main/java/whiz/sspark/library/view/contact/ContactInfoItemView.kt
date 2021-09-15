package whiz.sspark.library.view.contact

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ContactInfo
import whiz.sspark.library.data.enum.ContactType
import whiz.sspark.library.databinding.ViewContactInfoItemBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.toPhoneNumber

class ContactInfoItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewContactInfoItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(contactInfo: ContactInfo,
             onContactInfoClicked: (ContactInfo) -> Unit) {
        with(contactInfo) {
            when(type) {
                ContactType.TELEPHONE.type -> {
                    binding.tvContactInformation.text = displayTitle.toPhoneNumber()
                    binding.ivContact.setImageResource(R.drawable.ic_contact_phone)
                }
                ContactType.WEB.type -> {
                    binding.tvContactInformation.text = displayTitle
                    binding.ivContact.setImageResource(R.drawable.ic_contact_mail)
                }
            }

            binding.ivMore.show(R.drawable.ic_arrow_right)
            binding.tvContactTitle.text = contactTitle
        }

        setOnClickListener {
            onContactInfoClicked(contactInfo)
        }
    }
}