package whiz.tss.sspark.s_spark_android.presentation.collaboration

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.enum.BottomNavigationType
import whiz.sspark.library.extension.setGradientDrawable
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.BottomNavigationId
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.databinding.ActivityClassDetailBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_activity.instructor.InstructorClassActivityFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_activity.student.StudentClassActivityFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_assignment.student.StudentClassAssignmentFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_attendance.student.StudentClassAttendanceFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_member.student.StudentClassMemberFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.course_syllabus.CourseSyllabusBottomSheetDialog

open class ClassDetailActivity : BaseActivity() {

    companion object {
        private const val COURSE_SYLLABUS = "CourseSyllabus"
    }

    protected lateinit var binding: ActivityClassDetailBinding

    protected val classGroupId by lazy {
        intent?.getStringExtra("classGroupId") ?: ""
    }

    protected val startColor by lazy {
        intent?.getIntExtra("startColor", ContextCompat.getColor(this, R.color.primaryStartColor)) ?: ContextCompat.getColor(this, R.color.primaryStartColor)
    }

    protected val endColor by lazy {
        intent?.getIntExtra("endColor", ContextCompat.getColor(this, R.color.primaryEndColor)) ?: ContextCompat.getColor(this, R.color.primaryEndColor)
    }

    protected val allMemberCount by lazy {
        intent?.getIntExtra("allMemberCount", 0) ?: 0
    }

    private val title by lazy {
        intent?.getStringExtra("title") ?: ""
    }

    private val subTitle by lazy {
        intent?.getStringExtra("subTitle") ?: ""
    }

    protected val colors by lazy {
        intArrayOf(
            startColor,
            ContextCompat.getColor(this, R.color.textBaseThirdColor)
        )
    }

    protected open val bottomNavigationBarItems by lazy {
        listOf(
            BottomNavigationBarItem(id = BottomNavigationId.ACTIVITY.id, title = resources.getString(R.string.class_detail_tab_activity), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_activity, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.ATTENDANCE.id, title = resources.getString(R.string.class_detail_tab_attendance), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_attendance, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.ASSIGNMENT.id, title = resources.getString(R.string.class_detail_tab_homework), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_homework, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.MEMBER.id, title = resources.getString(R.string.class_detail_tab_student), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_member, colors = colors.toList())
        )
    }

    protected var currentFragment: Int = BottomNavigationId.NONE_SELECTED.id
    protected lateinit var currentTerm: Term

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            profileManager.term.collect {
                it?.let {
                    currentTerm = it
                    initView()
                }
            }
        }
    }

    override fun initView() {
        val gradientDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
            colors = intArrayOf(startColor, endColor)
        }

        with(binding.vProfile) {
            init(backgroundDrawable = gradientDrawable)

            if (SSparkLibrary.isDarkModeEnabled) {
                window.statusBarColor = ContextCompat.getColor(this@ClassDetailActivity, R.color.viewBaseSecondaryColor) //TODO change this line when darkmode is enable
            } else {
                window.setGradientDrawable(gradientDrawable)
            }
        }

        with(binding.vClassDetail) {
            init(
                backgroundDrawable = gradientDrawable,
                title = title,
                subTitle = subTitle,
                color = startColor,
                bottomNavigationBarItems =  bottomNavigationBarItems,
                onNavigationItemSelected = {
                    if (currentFragment != it) {
                        currentFragment = it

                        onNavigationItemSelected()
                    }
                },
                onStudyPlanClicked = {
                    val isShowing = supportFragmentManager.findFragmentByTag(COURSE_SYLLABUS) != null
                    if (!isShowing) {
                        CourseSyllabusBottomSheetDialog.newInstance(
                            classGroupId =  classGroupId,
                            termId = currentTerm.id,
                            startColor = startColor,
                            endColor = endColor
                        ).show(supportFragmentManager, COURSE_SYLLABUS)
                    }
                }
            )
        }
    }

    protected open fun onNavigationItemSelected() {
        when (currentFragment) {
            BottomNavigationId.ACTIVITY.id -> {
                when (SSparkApp.role) {
                    RoleType.INSTRUCTOR_JUNIOR,
                    RoleType.INSTRUCTOR_SENIOR -> binding.vClassDetail.renderFragment(InstructorClassActivityFragment.newInstance(classGroupId, startColor, endColor, allMemberCount), supportFragmentManager, currentFragment)
                    RoleType.STUDENT_JUNIOR,
                    RoleType.STUDENT_SENIOR -> binding.vClassDetail.renderFragment(StudentClassActivityFragment.newInstance(classGroupId, startColor, endColor, allMemberCount), supportFragmentManager, currentFragment)
                    RoleType.GUARDIAN -> { } //TODO wait implement
                }
            }
            BottomNavigationId.ATTENDANCE.id -> binding.vClassDetail.renderFragment(StudentClassAttendanceFragment.newInstance(classGroupId), supportFragmentManager, currentFragment)
            BottomNavigationId.ASSIGNMENT.id -> binding.vClassDetail.renderFragment(StudentClassAssignmentFragment.newInstance(classGroupId, startColor), supportFragmentManager, currentFragment)
            BottomNavigationId.MEMBER.id -> binding.vClassDetail.renderFragment(StudentClassMemberFragment.newInstance(classGroupId), supportFragmentManager, currentFragment)
        }
    }
}