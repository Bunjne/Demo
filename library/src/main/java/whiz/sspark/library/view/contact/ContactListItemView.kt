package whiz.sspark.library.view.contact

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.databinding.ViewContactListItemBinding
import whiz.sspark.library.extension.show

class ContactListItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewContactListItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(contact: Contact,
             onContactClicked: (Contact) -> Unit) {
        binding.ivMore.show(R.drawable.ic_arrow_right)

        with(contact) {
            binding.tvContactName.text = title
            binding.ivProfile.show(imageUrl)
        }

        setOnClickListener {
            onContactClicked(contact)
        }
    }
}