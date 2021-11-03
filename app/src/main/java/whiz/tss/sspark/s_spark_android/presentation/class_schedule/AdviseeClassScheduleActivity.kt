package whiz.tss.sspark.s_spark_android.presentation.class_schedule

import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.viewModel.AdviseeClassScheduleViewModel
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.utility.isPrimaryHighSchool
import whiz.tss.sspark.s_spark_android.presentation.class_schedule.all_class.AdviseeClassScheduleAllClassBottomSheetDialog

class AdviseeClassScheduleActivity: StudentClassScheduleActivity() {

    override val viewModel: AdviseeClassScheduleViewModel by viewModel()

    private val student: Student by lazy {
        intent?.getStringExtra("student")?.toObject<Student>()!!
    }

    private lateinit var advisee: Advisee

    private val menuTitle by lazy {
        intent?.getStringExtra("title") ?: ""
    }

    override fun initView() {
        super.initView()

        advisee = if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
            student.convertToJuniorAdvisee()
        } else {
            student.convertToSeniorAdvisee()
        }

        binding.vClassSchedule.showAdviseeProfile(advisee = advisee)
    }

    override fun showAllClassDialog() {
        AdviseeClassScheduleAllClassBottomSheetDialog.newInstance(
            term = currentTerm,
            studentId = student.id,
            advisee = advisee
        ).show(supportFragmentManager, ALL_CLASS_DIALOG)
    }

    override val title: String by lazy {
        menuTitle
    }

    override fun getTerms() {
        viewModel.getTerms(studentId = student.id)
    }
}