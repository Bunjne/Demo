package whiz.sspark.library.view.widget.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.databinding.ViewContactListItemBinding

class ContactAdapter(private val context: Context,
                     private val contacts: List<Contact>,
                     private val onContactClicked: (String, String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContactListItemViewHolder(ViewContactListItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contact = contacts.getOrNull(position)
        contact?.let { contact ->
            (holder as ContactListItemViewHolder).apply {
                init(contact, onContactClicked)
            }
        }
    }

    override fun getItemCount() = contacts.size
}