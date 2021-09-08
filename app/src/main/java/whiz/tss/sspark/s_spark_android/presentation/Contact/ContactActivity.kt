package whiz.tss.sspark.s_spark_android.presentation.Contact

import android.os.Bundle
import android.util.Log
import whiz.sspark.library.data.entity.Contact
import whiz.tss.sspark.s_spark_android.databinding.ActivityContactBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class ContactActivity : BaseActivity() {

    private lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        observeView()
        observeData()
        observeError()
    }

    override fun initView() {
        binding.vContact.init(
            onContactClicked = { contact ->

            },
            onRefresh = {

            }
        )
        binding.vContact.updateContactItems(Contact.getContacts())
    }

    override fun observeView() {

    }

    override fun observeData() {
    }

    override fun observeError() {
    }
}