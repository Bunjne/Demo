package whiz.tss.sspark.s_spark_android.presentation.contact

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.data.viewModel.ContactViewModel
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.databinding.ActivityContactBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class ContactActivity : BaseActivity() {

    private val viewModel: ContactViewModel by viewModel()

    private lateinit var binding: ActivityContactBinding

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

            },
            onRefresh = {
                viewModel.getContacts(true)
            }
        )
        binding.vContact.updateContactItems(Contact.getContacts())
    }

    override fun observeView() {
        viewModel.contactsLoading.observe(this) { isLoading ->
            binding.vContact.setSwipeRefreshLoading(isLoading)
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
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                showAlertWithOkButton(it)
            }
        }
    }
}