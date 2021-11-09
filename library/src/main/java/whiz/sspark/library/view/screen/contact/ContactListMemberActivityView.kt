package whiz.sspark.library.view.screen.contact

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.MenuContactInfoItem
import whiz.sspark.library.databinding.ViewContactListMemberActivityBinding
import whiz.sspark.library.extension.showViewStateX
import whiz.sspark.library.utility.updateItem
import whiz.sspark.library.view.general.custom_divider.CustomDividerItemDecoration
import whiz.sspark.library.view.widget.contact.ContactListMemberAdapter

class ContactListMemberActivityView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewContactListMemberActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String,
             contactMembers: List<MenuContactInfoItem>,
             onContactClicked: (MenuContactInfoItem) -> Unit,
             onRefresh: () -> Unit) {
        binding.tvTitle.text = title
        binding.srlContact.setOnRefreshListener {
            onRefresh()
        }

        with(binding.rvContact) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                CustomDividerItemDecoration(
                    divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                    dividerViewType = 0
                )
            )

            adapter = ContactListMemberAdapter(
                context = context,
                contactMembers =  contactMembers,
                onContactClicked = { contact ->
                    onContactClicked(contact)
                }
            )
        }
    }

    fun updateContactMemberItems(contactMembers: MutableList<MenuContactInfoItem>, updatedContactMembers: List<MenuContactInfoItem>) {
        binding.rvContact.adapter?.updateItem(contactMembers, updatedContactMembers)
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlContact.isRefreshing = isLoading == true
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }
}