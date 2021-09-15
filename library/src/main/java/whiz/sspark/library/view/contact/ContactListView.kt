package whiz.sspark.library.view.contact

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.databinding.ViewContactListBinding
import whiz.sspark.library.extension.showViewStateX

class ContactListView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewContactListBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val contactsLocal: MutableList<Contact> = mutableListOf()

    fun init(onContactClicked: (Contact) -> Unit,
             onRefresh: () -> Unit) {
        binding.srlContact.setOnRefreshListener {
         onRefresh()
        }

        with(binding.rvContact) {
            layoutManager = LinearLayoutManager(context)
            adapter = ContactAdapter(
                context = context,
                contacts =  contactsLocal
            ) { contact ->
                onContactClicked(contact)
            }
        }
    }

    fun updateContactItems(contacts: List<Contact>) {
        with(contactsLocal) {
            clear()
            addAll(contacts)
            addAll(contacts)
            addAll(contacts)
        }

        binding.rvContact?.adapter?.notifyDataSetChanged()
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlContact.isRefreshing = isLoading == true
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }
}