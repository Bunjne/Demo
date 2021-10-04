package whiz.tss.sspark.s_spark_android.presentation.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.MenuCode
import whiz.sspark.library.data.enum.MenuSegmentType
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.data.viewModel.MenuStudentViewModel
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.databinding.FragmentMenuBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.presentation.learning_pathway.LearningPathwayActivity
import whiz.tss.sspark.s_spark_android.presentation.school_record.SchoolRecordActivity
import whiz.tss.sspark.s_spark_android.utility.logout

class MenuStudentFragment : BaseFragment() {

    companion object {
        fun newInstance() = MenuStudentFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    private val viewModel: MenuStudentViewModel by viewModel()

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var student: Student
    private lateinit var termId: String
    private var menuContactInfoDialog: MenuContactInfoDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            profileManager.term.collect {
                it?.let {
                    termId = it.id
                }
            }
        }

        lifecycleScope.launch {
            profileManager.student.collect {
                it?.let {
                    student = it

                    initView()

                    viewModel.getMenu()
                } ?: run {
                    logout(requireContext())
                }
            }
        }
    }

    override fun initView() {
        val segments = if (SSparkApp.role == RoleType.JUNIOR) {
            listOf(MenuSegment(resources.getString(R.string.menu_junior_segment_instructor_text), MenuSegmentType.INSTRUCTOR),
                MenuSegment(resources.getString(R.string.menu_segment_guardian_text), MenuSegmentType.GUARDIAN))
        } else {
            listOf(MenuSegment(resources.getString(R.string.menu_senior_segment_instructor_text), MenuSegmentType.INSTRUCTOR),
                MenuSegment(resources.getString(R.string.menu_segment_guardian_text), MenuSegmentType.GUARDIAN))
        }

        binding.vMenu.init(
            student = student,
            segments = segments,
            onCameraClicked = {
                //TODO wait implement camera
            },
            onMemberClicked = {
                if (it.type == MenuSegmentType.INSTRUCTOR) {
                    student.advisor.getOrNull(it.index)?.let {
                        val isShown = menuContactInfoDialog?.isAdded ?: false

                        if (menuContactInfoDialog == null || !isShown) {
                            menuContactInfoDialog = MenuContactInfoDialog.newInstance(it.getAdvisorMenuInfoItem(requireContext()))
                            menuContactInfoDialog?.show(childFragmentManager, "advisorContactInfoDialog")
                        }
                    }
                } else {
                    student.guardians.getOrNull(it.index)?.let {
                        val isShown = menuContactInfoDialog?.isAdded ?: false

                        if (menuContactInfoDialog == null || !isShown) {
                            menuContactInfoDialog = MenuContactInfoDialog.newInstance(it.getGuardianMenuInfoItem(requireContext()))
                            menuContactInfoDialog?.show(childFragmentManager, "guardianContactInfoDialog")
                        }
                    }
                }
            },
            onMenuClicked = { code ->
                when(code) {
                    MenuCode.GRADE_SUMMARY.code -> {
                        val intent = Intent(requireContext(), SchoolRecordActivity::class.java)
                        startActivity(intent)
                    }
                    MenuCode.LEARNING_PATHWAY.code -> {
                        val intent = Intent(requireContext(), LearningPathwayActivity::class.java)
                        startActivity(intent)
                    }
                    //TODO wait implement other screen
                }
            },
            onRefresh = {
                viewModel.getMenu()
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vMenu.setSwipeRefreshLoading(isLoading)
        }
    }

    override fun observeData() {
        profileManager.student.asLiveData().observe(this) {
            it?.let {
                student = it
                binding.vMenu.updateStudentProfileImage(student.profileImageUrl, getGender(it.gender).type)
            }
        }

        viewModel.menuResponse.observe(this) {
            it?.let {
                binding.vMenu.updateMenu(it)
                viewModel.fetchWidget(it, termId)
            }
        }

        viewModel.advisingNoteResponse.observe(this) {
            it?.let {
                binding.vMenu.updateAdvisingNote(it)
            }
        }

        viewModel.calendarResponse.observe(this) {
            it?.let {
                binding.vMenu.updateCalendar(it)
            }
        }

        viewModel.notificationInboxResponse.observe(this) {
            it?.let {
                binding.vMenu.updateNotificationInbox(it)
            }
        }

        viewModel.gradeSummaryResponse.observe(this) {
            it?.let {
                binding.vMenu.updateGradeSummary(it)
            }
        }
    }

    override fun observeError() {
        viewModel.menuErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(requireContext(), it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                requireContext().showAlertWithOkButton(it)
            }
        }
    }
}