package whiz.tss.sspark.s_spark_android.presentation.learning_pathway

import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Student
import whiz.sspark.library.data.entity.convertToJuniorAdvisee
import whiz.sspark.library.data.entity.convertToSeniorAdvisee
import whiz.sspark.library.data.viewModel.AdviseeLearningPathwayViewModel
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.utility.isPrimaryHighSchool

open class AdviseeLearningPathwayActivity : LearningPathwayActivity() {

    private val student by lazy {
        intent?.getStringExtra("student")!!.toObject<Student>()!!
    }

    override val viewModel: AdviseeLearningPathwayViewModel by viewModel()

    override val isPlanEditable = false

    override fun initView() {
        super.initView()

        val advisee = if (isPrimaryHighSchool(student.term.academicGrade ?: 0)) {
            student.convertToJuniorAdvisee()
        } else {
            student.convertToSeniorAdvisee()
        }

        binding.vLearningPathway.showAdviseeProfile(advisee)
    }

    override fun getLearningPathway() {
        viewModel.getLearningPathway(student.id)
    }
}