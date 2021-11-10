package whiz.sspark.library.view.widget.contact

import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.MenuContactInfoItem
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewContactListMemberItemBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle

class ContactListMemberViewHolder(
    private val binding: ViewContactListMemberItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun init(contactInfo: MenuContactInfoItem,
             onContactClicked: (MenuContactInfoItem) -> Unit) {
        binding.ivMore.show(R.drawable.ic_arrow_right)

        with(contactInfo) {
            binding.tvContactName.text = name
            binding.ivProfile.showUserProfileCircle(imageUrl, getGender(gender).type)
        }

        binding.root.setOnClickListener {
            onContactClicked(contactInfo)
        }
    }
}