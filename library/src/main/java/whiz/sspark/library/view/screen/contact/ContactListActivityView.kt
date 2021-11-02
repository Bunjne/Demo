package whiz.sspark.library.view.screen.contact

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.databinding.ViewContactListBinding
import whiz.sspark.library.extension.showViewStateX
import whiz.sspark.library.utility.updateItem
import whiz.sspark.library.view.widget.contact.ContactAdapter

class ContactListActivityView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewContactListBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String,
             contacts: List<Contact>,
             onContactClicked: (String, String) -> Unit,
             onRefresh: () -> Unit) {
        binding.tvTitle.text = title
        binding.srlContact.setOnRefreshListener {
            onRefresh()
        }

        with(binding.rvContact) {
            layoutManager = LinearLayoutManager(context)
            adapter = ContactAdapter(
                context = context,
                contacts =  contacts,
                onContactClicked = { contactGroupId, groupName ->
                    onContactClicked(contactGroupId, groupName)
                }
            )
        }
    }

    fun updateContactItems(contacts: MutableList<Contact>, updatedContacts: List<Contact>) {
        binding.rvContact.adapter?.updateItem(contacts, updatedContacts)
        setViewVisibility(updatedContacts)
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlContact.isRefreshing = isLoading == true
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    private fun setViewVisibility(contacts: List<Contact>) {
        with(binding) {
            if (contacts.isNullOrEmpty()) {
                tvNoAnnouncement.visibility = View.VISIBLE
                rvContact.visibility = View.GONE
            } else {
                tvNoAnnouncement.visibility = View.GONE
                rvContact.visibility = View.VISIBLE
            }
        }
    }
}