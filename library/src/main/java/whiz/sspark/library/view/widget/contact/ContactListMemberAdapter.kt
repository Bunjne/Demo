package whiz.sspark.library.view.widget.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.MenuContactInfoItem
import whiz.sspark.library.databinding.ViewContactListMemberItemBinding
import whiz.sspark.library.extension.setDarkModeBackground

class ContactListMemberAdapter(private val context: Context,
                               private val contactMembers: List<MenuContactInfoItem>,
                               private val onContactClicked: (MenuContactInfoItem) -> Unit) : RecyclerView.Adapter<ContactListMemberViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListMemberViewHolder {
        return ContactListMemberViewHolder(ViewContactListMemberItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ContactListMemberViewHolder, position: Int) {
        val item = contactMembers.getOrNull(position)
        val isNextItemTitle = position == contactMembers.lastIndex
        val isPreviousItemTitle = position == 0

        item?.let {
            holder.init(it, onContactClicked)
            holder.itemView.setDarkModeBackground(isNextItemTitle, isPreviousItemTitle)
        }
    }

    override fun getItemCount() = contactMembers.size
}