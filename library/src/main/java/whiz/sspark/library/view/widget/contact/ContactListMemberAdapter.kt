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
                               private val onContactClicked: (MenuContactInfoItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContactListMemberViewHolder(ViewContactListMemberItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = contactMembers.getOrNull(position)
        val isNextItemTitle = position == contactMembers.lastIndex
        val isPreviousItemTitle = position == 0

        item?.let {
            with(holder) {
                (this as ContactListMemberViewHolder).init(it, onContactClicked)

                itemView.setDarkModeBackground(isNextItemTitle, isPreviousItemTitle)
            }
        }
    }

    override fun getItemCount() = contactMembers.size
}