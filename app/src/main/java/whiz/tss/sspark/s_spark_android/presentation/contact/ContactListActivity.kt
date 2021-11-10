package whiz.tss.sspark.s_spark_android.presentation.contact

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.viewModel.ContactViewModel
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityContactBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class ContactListActivity : BaseActivity(){

    private val viewModel: ContactViewModel by viewModel()

    private lateinit var binding: ActivityContactBinding

    private val title by lazy {
        intent?.getStringExtra("title") ?: ""
    }
    private var dataWrapper: DataWrapperX<Any>? = null
    private val contacts: MutableList<Contact> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

        if (dataWrapper != null) {
            val savedContacts = dataWrapper?.data?.toJson()?.toObjects(Array<Contact>::class.java) ?: listOf()

            binding.vContact.setLatestUpdatedText(dataWrapper)
            binding.vContact.updateContactItems(contacts, savedContacts)
        } else {
            viewModel.getContacts()
        }
    }

    override fun initView() {
        binding.vProfile.init(lifecycle)

        binding.vContact.init(
            title = title,
            contacts = contacts,
            onContactClicked = { contactGroupId, groupName ->
                val intent = Intent(this@ContactListActivity, ContactListMemberActivity::class.java)
                intent.putExtra("contactGroupId", contactGroupId)
                intent.putExtra("groupName", groupName)
                startActivity(intent)
            },
            onRefresh = {
                viewModel.getContacts()
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vContact.setSwipeRefreshLoading(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            dataWrapper = it
            binding.vContact.setLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.contactsResponse.observe(this) {
            it?.let {
                binding.vContact.updateContactItems(contacts, it)
            }
        }
    }

    override fun observeError() {
        viewModel.contactsErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(this, it) {
                    finish()
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                showAlertWithOkButton(it)
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        dataWrapper = savedInstanceState.getString("dataWrapper")?.toObject<DataWrapperX<Any>>()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("dataWrapper", dataWrapper?.toJson())
    }
}