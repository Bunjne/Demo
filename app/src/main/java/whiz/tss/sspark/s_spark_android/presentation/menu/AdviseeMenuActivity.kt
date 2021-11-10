package whiz.tss.sspark.s_spark_android.presentation.menu

import android.content.Intent
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.MenuCode
import whiz.sspark.library.data.viewModel.AdviseeMenuViewModel
import whiz.sspark.library.extension.setGradientDrawable
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.menu.AdviseeGuardianAdapter
import whiz.sspark.library.view.widget.menu.MenuAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.databinding.ActivityAdviseeMenuBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.learning_pathway.AdviseeLearningPathwayActivity
import whiz.tss.sspark.s_spark_android.presentation.class_schedule.AdviseeClassScheduleActivity
import whiz.tss.sspark.s_spark_android.presentation.school_record.AdviseeSchoolRecordActivity

class AdviseeMenuActivity : BaseActivity() {

    companion object {
        const val GUARDIAN_CONTACT_INFO_DIALOG = "guardianContactInfoDialog"
    }

    private val viewModel: AdviseeMenuViewModel by viewModel()

    private lateinit var binding: ActivityAdviseeMenuBinding

    private val studentId by lazy {
        intent?.getStringExtra("studentId") ?: ""
    }

    private val advisee by lazy {
        intent?.getStringExtra("advisee")?.toObject<Advisee>()
    }

    private var student: Student? = null

    private var menus: List<MenuDTO> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdviseeMenuBinding.inflate(layoutInflater)
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

        initView()

        if (student != null) {
            updateAdviseeInfo()
            updateMenuItem()
        } else {
            viewModel.getAdviseeInfo(studentId)
            viewModel.getAdviseeMenu(studentId)
        }
    }

    override fun initView() {
        binding.vAdviseeMenu.init(
            advisee = advisee,
            onMemberClicked = {
                student?.guardians?.getOrNull(it.index)?.let {
                    val isShowing = supportFragmentManager.findFragmentByTag(GUARDIAN_CONTACT_INFO_DIALOG) != null

                    if (!isShowing) {
                        val guardianInfo = it.getGuardianMenuInfoItem(this)

                        MenuContactInfoDialog.newInstance(
                            menuContactItem = guardianInfo
                        ).show(supportFragmentManager, GUARDIAN_CONTACT_INFO_DIALOG)
                    }
                }
            },
            onMenuClicked = { code, title ->
                when (code) {
                    MenuCode.LEARNING_OUTCOME.code -> {
                        val intent = Intent(this, AdviseeSchoolRecordActivity::class.java).apply {
                            putExtra("student", student?.toJson())
                        }
                        startActivity(intent)
                    }
                    MenuCode.LEARNING_PATHWAY.code -> {
                        val intent = Intent(this, AdviseeLearningPathwayActivity::class.java).apply {
                            putExtra("student", student?.toJson())
                        }
                        startActivity(intent)
                    }
                    MenuCode.ADVISEE_CLASS_SCHEDULE.code -> {
                        val intent = Intent(this, AdviseeClassScheduleActivity::class.java).apply {
                            putExtra("student", student?.toJson())
                            putExtra("title", title)
                        }
                        startActivity(intent)
                    }
                }
            },
            onRefresh = {
                viewModel.getAdviseeInfo(studentId)
                viewModel.getAdviseeMenu(studentId)
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vAdviseeMenu.setSwipeRefreshLayout(isLoading)
        }
    }

    override fun observeData() {
        viewModel.studentResponse.observe(this) {
            it?.let {
                student = it
                updateAdviseeInfo()
            }
        }

        viewModel.menuResponse.observe(this) {
            it?.let {
                menus = it
                updateMenuItem()
            }
        }
    }

    override fun observeError() {
        viewModel.menuErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.studentErrorResponse.observe(this) {
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

    private fun updateAdviseeInfo() {
        val convertedAdviseeInfo = if (SSparkApp.role == RoleType.INSTRUCTOR_JUNIOR) {
            student?.convertToJuniorAdvisee()
        } else {
            student?.convertToSeniorAdvisee()
        }

        binding.vAdviseeMenu.updateAdviseeInfo(convertedAdviseeInfo)

        if (!student?.getGuardian().isNullOrEmpty()) {
            val convertedGuardian = mutableListOf<AdviseeGuardianAdapter.AdviseeGuardianItem>()

            val title = AdviseeGuardianAdapter.AdviseeGuardianItem.Title(resources.getString(R.string.menu_segment_guardian_text))
            convertedGuardian.add(title)

            student?.getGuardian()?.forEach {
                val guardian = AdviseeGuardianAdapter.AdviseeGuardianItem.Guardian(it)
                convertedGuardian.add(guardian)
            }

            binding.vAdviseeMenu.updateGuardian(convertedGuardian)
        }
    }

    private fun updateMenuItem() {
        val items = mutableListOf<MenuAdapter.Item>()

        menus.forEach { menuDTO ->
            items.add(MenuAdapter.Item(type = menuDTO.type, code = menuDTO.code, title = menuDTO.name))

            menuDTO.items.forEach { menuItemDTO ->
                items.add(MenuAdapter.Item(type = menuItemDTO.type, title = menuItemDTO.name, code = menuItemDTO.code, menuItem = menuItemDTO.convertToAdapterItem()))
            }
        }

        binding.vAdviseeMenu.updateMenu(items)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        student = savedInstanceState.getString("student")?.toObject<Student>()
        menus = savedInstanceState.getString("menus")?.toObjects(Array<MenuDTO>::class.java) ?: listOf()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("student", student?.toJson())
        outState.putString("menus", menus.toJson())
    }
}