package whiz.sspark.library.view.contact

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.extension.toDP

class ContactAdapter(private val context: Context,
                     private val contacts: List<Contact>,
                     private val onContactClicked: (Contact) -> Unit) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ContactListItemView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts.getOrNull(position)
        contact?.let { contact ->
            (holder.itemView as? ContactListItemView)?.apply {
                init(contact, onContactClicked)

                setPadding(0, 0, 0, 8.toDP(context))
            }
        }
    }

    override fun getItemCount() = contacts.size
}