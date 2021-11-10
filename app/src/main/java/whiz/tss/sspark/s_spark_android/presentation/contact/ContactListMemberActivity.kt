package whiz.tss.sspark.s_spark_android.presentation.contact

import android.os.Bundle
import android.os.PersistableBundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.MenuContactInfoItem
import whiz.sspark.library.data.entity.StudentInstructorDTO
import whiz.sspark.library.data.entity.getAdvisorMenuInfoItem
import whiz.sspark.library.data.viewModel.ContactMemberViewModel
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.databinding.ActivityContactListMemberBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.menu.MenuContactInfoDialog

class ContactListMemberActivity : BaseActivity() {

    companion object {
        const val CONTACT_INFO_DIALOG = "advisorContactInfoDialog"
    }

    private val viewModel: ContactMemberViewModel by viewModel()

    private lateinit var binding: ActivityContactListMemberBinding

    private var menuContactInfoDialog: MenuContactInfoDialog? = null

    private var dataWrapper: DataWrapperX<Any>? = null
    private val contactMembers: MutableList<MenuContactInfoItem> = mutableListOf()

    private val contactGroupId by lazy {
        intent?.getStringExtra("contactGroupId") ?: ""
    }

    private val title by lazy {
        intent?.getStringExtra("groupName") ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactListMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

        if (dataWrapper != null) {
            val savedContactMembers = dataWrapper?.data?.toJson()?.toObjects(Array<MenuContactInfoItem>::class.java) ?: listOf()

            binding.vContact.setLatestUpdatedText(dataWrapper)
            binding.vContact.updateContactMemberItems(contactMembers, savedContactMembers)
        } else {
            viewModel.getContactMembers(contactGroupId)
        }
    }

    override fun initView() {
        binding.vProfile.init(lifecycle)

        binding.vContact.init(
            title = title,
            contactMembers = contactMembers,
            onContactClicked = { contact ->
                val isShowing = menuContactInfoDialog?.isAdded ?: false

                if (!isShowing) {
                    menuContactInfoDialog = MenuContactInfoDialog.newInstance(contact)
                    menuContactInfoDialog?.show(supportFragmentManager, CONTACT_INFO_DIALOG)
                }
            },
            onRefresh = {
                viewModel.getContactMembers(contactGroupId)
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
        viewModel.contactMembersResponse.observe(this) {
            it?.let {
                val members = getContactMembers(it)
                binding.vContact.updateContactMemberItems(contactMembers, members)
            }
        }
    }

    override fun observeError() {
        viewModel.contactMembersErrorResponse.observe(this) {
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

    private fun getContactMembers(members: List<StudentInstructorDTO>): List<MenuContactInfoItem> {
        return members.map {
            it.getAdvisorMenuInfoItem(this)
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