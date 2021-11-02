package whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.LearningOutcome
import whiz.sspark.library.data.entity.LearningOutcomeDTO
import whiz.sspark.library.data.entity.Student
import whiz.sspark.library.data.viewModel.AdviseeLearningOutcomeViewModel
import whiz.sspark.library.data.viewModel.LearningOutcomeViewModel
import whiz.sspark.library.extension.*
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.school_record.learning_outcome.JuniorLearningOutcomeAdapter
import whiz.tss.sspark.s_spark_android.databinding.FragmentJuniorLearningOutcomeBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.presentation.school_record.expect_outcome.AdviseeJuniorExpectOutcomeBottomSheetDialog
import whiz.tss.sspark.s_spark_android.presentation.school_record.expect_outcome.JuniorExpectOutcomeBottomSheetDialog

class AdviseeJuniorLearningOutcomeFragment : JuniorLearningOutcomeFragment() {

    companion object {
        fun newInstance(termId: String, student: Student) = AdviseeJuniorLearningOutcomeFragment().apply {
            arguments = Bundle().apply {
                putString("termId", termId)
                putString("student", student.toJson())
            }
        }
    }

    override val viewModel: AdviseeLearningOutcomeViewModel by viewModel()

    private val student by lazy {
        arguments?.getString("student")?.toObject<Student>()!!
    }

    override fun getLearningOutcome() {
        viewModel.getLearningOutcome(termId, student.id)
    }

    override fun showJuniorExpectOutcome(learningOutcome: LearningOutcome) {
        AdviseeJuniorExpectOutcomeBottomSheetDialog.newInstance(
            termId = termId,
            student = student,
            courseId = learningOutcome.courseId,
            courseCode = learningOutcome.courseCode,
            courseName = learningOutcome.courseName,
            credit = learningOutcome.credit
        ).show(childFragmentManager, EXPECT_OUTCOME_TAG)
    }
}