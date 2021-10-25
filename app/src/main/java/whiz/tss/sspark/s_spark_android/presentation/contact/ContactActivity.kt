package whiz.tss.sspark.s_spark_android.presentation.contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.ContactInfo
import whiz.sspark.library.data.enum.ContactType
import whiz.sspark.library.data.viewModel.ContactViewModel
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.databinding.ActivityContactBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class ContactActivity : BaseActivity(), ContactInfoDialogFragment.DialogOnClickListener {

    private val viewModel: ContactViewModel by viewModel()

    private lateinit var binding: ActivityContactBinding

    private var alertDialog: ContactInfoDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        observeView()
        observeData()
        observeError()

        viewModel.getContacts(false)
    }

    override fun initView() {
        binding.vContact.init(
            onContactClicked = { contact ->
                val isShowing = alertDialog?.isAdded ?: false

                if (!isShowing) {
                    alertDialog = ContactInfoDialogFragment.newInstance(contact.toJson())
                    alertDialog?.show(supportFragmentManager, "")
                }
            },
            onRefresh = {
                viewModel.getContacts(true)
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vContact.setSwipeRefreshLoading(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            binding.vContact.setLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.contactsResponse.observe(this) {
            it?.let {
                binding.vContact.updateContactItems(it)
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

    override fun onContactInfoSelected(contactInfo: ContactInfo) {
        with (contactInfo) {
            when (type) {
                ContactType.WEB.type -> openUrl(contactInfo)
                ContactType.TELEPHONE.type -> {
                    val phoneNumber = value.filter { it != '-' }
                    if (phoneNumber.length >= 10 && phoneNumber.take(2) != "02") {
                        callPhone(phoneNumber.take(10))
                    } else if (phoneNumber.take(2) == "02") {
                        callPhone(phoneNumber.take(9))
                    }
                }
            }
        }
    }

    private fun openUrl(contact: ContactInfo) {
        val navigatedUrl = if (contact.value.contains("line.me")) {
            contact.value.replaceAfter("p/", contact.displayTitle)
        } else {
            contact.value
        }
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(navigatedUrl)))
    }

    private fun callPhone(phoneNumber: String) {
        startActivity(Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        })
    }
}