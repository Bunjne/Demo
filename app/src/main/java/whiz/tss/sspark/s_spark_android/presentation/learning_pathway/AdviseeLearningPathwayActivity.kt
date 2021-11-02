package whiz.tss.sspark.s_spark_android.presentation.learning_pathway

import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.viewModel.AdviseeLearningPathwayViewModel
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.utility.isPrimaryHighSchool
import whiz.tss.sspark.s_spark_android.presentation.learning_pathway.basic_course.AdviseeBasicCourseBottomSheetDialog

open class AdviseeLearningPathwayActivity : LearningPathwayActivity() {

    override val viewModel: AdviseeLearningPathwayViewModel by viewModel()

    private val student by lazy {
        intent?.getStringExtra("student")!!.toObject<Student>()!!
    }

    private lateinit var advisee: Advisee

    override val isPlanEditable = false

    override fun initView() {
        super.initView()

        advisee = if (isPrimaryHighSchool(student.term.academicGrade ?: 0)) {
            student.convertToJuniorAdvisee()
        } else {
            student.convertToSeniorAdvisee()
        }

        binding.vLearningPathway.showAdviseeProfile(advisee)
    }

    override fun getLearningPathway() {
        viewModel.getLearningPathway(student.id)
    }

    override fun showBasicCoursesDialog(term: Term, courses: List<Course>) {
        AdviseeBasicCourseBottomSheetDialog.newInstance(
            term = term,
            courses = courses,
            advisee = advisee
        ).show(supportFragmentManager, BASIC_COURSE_DIALOG)
    }
}