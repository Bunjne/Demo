package whiz.sspark.library.view.widget.contact

import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.databinding.ViewContactListItemBinding
import whiz.sspark.library.extension.show

class ContactListItemViewHolder(private val binding: ViewContactListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun init(contact: Contact,
             onContactClicked: (String, String) -> Unit) {
        binding.ivMore.show(R.drawable.ic_arrow_right)

        with(contact) {
            binding.tvContactName.text = name
            binding.ivProfile.show(iconImageUrl)

            binding.root.setOnClickListener {
                onContactClicked(id, name)
            }
        }
    }
}