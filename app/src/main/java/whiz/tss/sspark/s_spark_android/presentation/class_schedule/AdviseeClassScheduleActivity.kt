package whiz.tss.sspark.s_spark_android.presentation.class_schedule

import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.viewModel.AdviseeClassScheduleViewModel
import whiz.sspark.library.extension.toObject
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType

class AdviseeClassScheduleActivity: StudentClassScheduleActivity() {

    override val viewModel: AdviseeClassScheduleViewModel by viewModel()

    private val student: Student by lazy {
        intent?.getStringExtra("student")?.toObject<Student>()!!
    }

    private val menuTitle by lazy {
        intent?.getStringExtra("title") ?: ""
    }

    override fun initView() {
        super.initView()

        val advisee = if (SSparkApp.role == RoleType.INSTRUCTOR_JUNIOR) {
            student.convertToJuniorAdvisee()
        } else {
            student.convertToSeniorAdvisee()
        }

        binding.vClassSchedule.showAdviseeProfile(advisee = advisee)
    }

    override val title: String = menuTitle

    override fun getTerms() {
        viewModel.getTerms(studentId = student.id)
    }
}