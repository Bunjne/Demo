package whiz.tss.sspark.s_spark_android.presentation.school_record.expect_outcome

import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Student
import whiz.sspark.library.data.entity.convertToJuniorAdvisee
import whiz.sspark.library.data.entity.convertToSeniorAdvisee
import whiz.sspark.library.data.viewModel.AdviseeExpectOutcomeViewModel
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType

class AdviseeSeniorExpectOutcomeBottomSheetDialog: SeniorExpectOutcomeBottomSheetDialog() {

    companion object {
        fun newInstance(termId: String, student: Student, courseId: String, courseCode: String, courseName: String, credit: Int) = AdviseeSeniorExpectOutcomeBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("termId", termId)
                putString("student", student.toJson())
                putString("courseId", courseId)
                putString("courseCode", courseCode)
                putString("courseName", courseName)
                putInt("credit", credit)
            }
        }
    }

    override val viewModel: AdviseeExpectOutcomeViewModel by viewModel()

    private val student by lazy {
        arguments?.getString("student")?.toObject<Student>()!!
    }

    override fun initView() {
        super.initView()

        val advisee = if (SSparkApp.role == RoleType.INSTRUCTOR_JUNIOR) {
            student.convertToJuniorAdvisee()
        } else {
            student.convertToSeniorAdvisee()
        }

        binding.vExpectOutcome.showAdviseeProfile(advisee)
    }

    override fun getExpectOutcome() {
        viewModel.getExpectOutcome(
            courseId = courseId,
            termId = termId,
            studentId = student.id
        )
    }
}