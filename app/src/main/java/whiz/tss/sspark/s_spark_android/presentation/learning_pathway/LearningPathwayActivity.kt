package whiz.tss.sspark.s_spark_android.presentation.learning_pathway

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.viewModel.LearningPathwayViewModel
import whiz.sspark.library.extension.setGradientDrawable
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showAlert
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.learning_pathway.LearningPathwayAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityLearningPathwayBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.learning_pathway.add_course.AddCourseBottomSheetDialog
import whiz.tss.sspark.s_spark_android.presentation.learning_pathway.basic_course.BasicCourseBottomSheetDialog

open class LearningPathwayActivity : BaseActivity(), AddCourseBottomSheetDialog.OnClickListener {

    companion object {
        internal const val ADD_COURSE_DIALOG = "AddCourseDialog"
        internal const val BASIC_COURSE_DIALOG = "BasicCourseDialog"
    }

    protected open val viewModel: LearningPathwayViewModel by viewModel()

    protected lateinit var binding: ActivityLearningPathwayBinding

    protected open val isPlanEditable = false // this flag is depend on requirement

    private var dataWrapper: DataWrapperX<Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearningPathwayBinding.inflate(layoutInflater)
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        setContentView(binding.root)

        initView()

        if (savedInstanceState != null) {
            onSaveInstanceState(savedInstanceState)

            if (dataWrapper != null) {
                val learningPathwaysDTO = dataWrapper?.data?.toJson()?.toObjects(Array<LearningPathwayDTO>::class.java) ?: listOf()

                binding.vLearningPathway.setLatestUpdatedText(dataWrapper)
                updateAdapterItem(learningPathwaysDTO)
            } else {
                getLearningPathway()
            }
        } else {
            getLearningPathway()
        }
    }

    protected open fun getLearningPathway() {
        viewModel.getLearningPathway()
    }

    override fun initView() {
        binding.vProfile.init(lifecycle)
        binding.vLearningPathway.init(
            isPlanEditable = isPlanEditable,
            onAddCourseClicked = { term, currentCredit, minCredit, maxCredit, selectedCourseIds ->
                if (viewModel.viewLoading.value == false) {
                    val isShowing = supportFragmentManager.findFragmentByTag(ADD_COURSE_DIALOG) != null

                    if (!isShowing) {
                        AddCourseBottomSheetDialog.newInstance(
                            term = term,
                            currentCredit = currentCredit,
                            minCredit = minCredit,
                            maxCredit = maxCredit,
                            selectedCourseIds = selectedCourseIds
                        ).show(supportFragmentManager, ADD_COURSE_DIALOG)
                    }
                }
            },
            onDeleteCourseClicked = { term, academicGrade, courseId ->
                if (viewModel.viewLoading.value == false) {
                    showAlert(
                        title = resources.getString(R.string.general_confirm_to_delete_text),
                        positiveTitle = resources.getString(R.string.general_delete_text),
                        onPositiveClicked = {
                            viewModel.deleteCourse(
                                term = term,
                                academicGrade = academicGrade,
                                courseId = courseId
                            )
                        },
                        negativeTitle = resources.getString(R.string.general_text_cancel)
                    )
                }
            },
            onShowBasicCourseClicked = { term, courses ->
                if (viewModel.viewLoading.value == false) {
                    val isShowing = supportFragmentManager.findFragmentByTag(BASIC_COURSE_DIALOG) != null

                    if (!isShowing) {
                        showBasicCoursesDialog(term, courses)
                    }
                }
            },
            onRefresh = {
                getLearningPathway()
            }
        )
    }

    override fun observeView() {
        viewModel.courseLoading.observe(this) { isLoading ->
            if (isLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }

        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vLearningPathway.setSwipeRefreshLoading(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            dataWrapper = it
            binding.vLearningPathway.setLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.learningPathwayResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                updateAdapterItem(it)
            }
        }

        viewModel.addCourseResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                getLearningPathway()
            }
        }

        viewModel.deleteCourseResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                getLearningPathway()
            }
        }
    }

    override fun observeError() {
        listOf(
            viewModel.learningPathwayErrorResponse,
            viewModel.addCourseErrorResponse,
            viewModel.deleteCourseErrorResponse
        ).forEach {
            it.observe(this) {
                it?.getContentIfNotHandled()?.let {
                    showApiResponseXAlert(this, it)
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.getContentIfNotHandled()?.let {
                showAlertWithOkButton(it)
            }
        }
    }

    protected open fun showBasicCoursesDialog(term: Term, courses: List<Course>) {
        BasicCourseBottomSheetDialog.newInstance(
            term = term,
            courses = courses
        ).show(supportFragmentManager, BASIC_COURSE_DIALOG)
    }

    private fun updateAdapterItem(learningPathways: List<LearningPathwayDTO>) {
        val items: MutableList<LearningPathwayAdapter.Item> = mutableListOf()

        learningPathways.forEach { learningPathway ->
            val summaryCourseCredit = learningPathway.courses.sumOf { it.credit }
            val summaryBasicCourseCredit = learningPathway.basicCourses.sumOf { it.credit }
            val credit = summaryCourseCredit + summaryBasicCourseCredit

            val courseIds = learningPathway.courses.map { it.id }
            val basicCourseIds = learningPathway.basicCourses.map { it.id }

            val selectedCourseIds = mutableListOf<String>()
            selectedCourseIds.addAll(courseIds)
            selectedCourseIds.addAll(basicCourseIds)

            val basicCourses = learningPathway.basicCourses.map {
                Course(
                    id = it.id,
                    code = it.code,
                    credit = it.credit,
                    name = it.name
                )
            }

            val header = LearningPathwayHeaderItem(
                term = learningPathway.term,
                currentCredit = credit,
                maxCredit = learningPathway.maxCredit,
                minCredit = learningPathway.minCredit,
                selectedCourseIds = selectedCourseIds,
                basicCourses = basicCourses
            )

            items.add(LearningPathwayAdapter.Item(header = header))

            if (learningPathway.courses.isNotEmpty()) {
                items.add(LearningPathwayAdapter.Item(courseCount = learningPathway.courses.size))

                learningPathway.courses.forEach {
                    val course = Course(
                        id = it.id,
                        code = it.code,
                        credit = it.credit,
                        name = it.name
                    )

                    val learningPathwayCourse = LearningPathwayCourseItem(
                        term = learningPathway.term,
                        course = course
                    )

                    items.add(LearningPathwayAdapter.Item(courseItem = learningPathwayCourse))
                }
            }
        }

        binding.vLearningPathway.updateItem(items)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        dataWrapper = savedInstanceState.getString("dataWrapper")?.toObject()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("dataWrapper", dataWrapper.toString())
    }

    override fun onAddCourseClicked(term: Int, academicGrade: Int, courseId: String) {
        viewModel.addCourse(
            term = term,
            academicGrade = academicGrade,
            courseId = courseId
        )
    }
}