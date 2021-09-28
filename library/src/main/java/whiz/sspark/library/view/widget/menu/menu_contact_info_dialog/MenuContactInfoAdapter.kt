package whiz.sspark.library.view.widget.menu.menu_contact_info_dialog

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.MenuContactItem

class MenuContactInfoAdapter(private val context: Context,
                             private val menuContactItems: List<MenuContactItem>,
                             private val onContactClicked: (description: String, contact: String) -> Unit): RecyclerView.Adapter<MenuContactInfoAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MenuContactInfoItemView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menuContactItem = menuContactItems.getOrNull(position)
        menuContactItem?.let {
            (holder.itemView as? MenuContactInfoItemView)?.init(
                contactIconRes = it.contactIconRes,
                contact = it.contact,
                contactDescription = it.contactDescription,
                onContactClicked = {
                    onContactClicked(it.contactDescription, it.contact)
                }
            )
        }
    }

    override fun getItemCount(): Int = menuContactItems.size
}