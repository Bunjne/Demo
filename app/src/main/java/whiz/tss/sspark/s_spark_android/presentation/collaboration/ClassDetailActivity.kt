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
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_attendance.student.StudentClassAttendanceFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_member.student.StudentClassMemberFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.course_syllabus.CourseSyllabusFragment

class ClassDetailActivity : BaseActivity() {

    companion object {
        private const val COURSE_SYLLABUS = "CourseSyllabus"
    }

    private lateinit var binding: ActivityClassDetailBinding

    private val classGroupId by lazy {
        intent?.getStringExtra("classGroupId") ?: ""
    }

    private val startColor by lazy {
        intent?.getIntExtra("color", ContextCompat.getColor(this, R.color.primaryStartColor)) ?: ContextCompat.getColor(this, R.color.primaryStartColor)
    }

    private val endColor by lazy {
        intent?.getIntExtra("endColor", ContextCompat.getColor(this, R.color.primaryEndColor)) ?: ContextCompat.getColor(this, R.color.primaryEndColor)
    }

    private val allMemberCount by lazy {
        intent?.getIntExtra("allMemberCount", 0) ?: 0
    }

    private val courseCode by lazy {
        intent?.getStringExtra("courseCode") ?: ""
    }

    private val courseName by lazy {
        intent?.getStringExtra("courseName") ?: ""
    }

    private var currentFragment: Int = BottomNavigationId.NONE_SELECTED.id
    private lateinit var currentTerm: Term

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

        with (binding.vProfile) {
            init(backgroundDrawable = gradientDrawable)

            if (SSparkLibrary.isDarkModeEnabled) {
                window.statusBarColor = ContextCompat.getColor(this@ClassDetailActivity, R.color.viewBaseSecondaryColor) //TODO change this line when darkmode is enable
            } else {
                window.setGradientDrawable(gradientDrawable)
            }
        }

        val colors = intArrayOf(
            startColor,
            ContextCompat.getColor(this, R.color.textBaseThirdColor)
        )

        val bottomNavigationBarItems = mutableListOf(
            BottomNavigationBarItem(id = BottomNavigationId.ACTIVITY.id, title = resources.getString(R.string.class_detail_tab_activity), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_activity, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.ATTENDANCE.id, title = resources.getString(R.string.class_detail_tab_attendance), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_attendance, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.STUDENT.id, title = resources.getString(R.string.class_detail_tab_student), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_member, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.ASSIGNMENT.id, title = resources.getString(R.string.class_detail_tab_homework), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_homework, colors = colors.toList())
        )

        with (binding.vClassDetail) {
            init(
                backgroundDrawable = gradientDrawable,
                courseName = courseName,
                courseCode = courseCode,
                color = startColor,
                bottomNavigationBarItems =  bottomNavigationBarItems,
                onNavigationItemSelected = {
                    if (currentFragment != it) {
                        currentFragment = it
                        when (it) {
                            BottomNavigationId.ACTIVITY.id -> {
                                if (SSparkApp.role != RoleType.INSTRUCTOR) {
                                    renderFragment(StudentClassActivityFragment.newInstance(classGroupId, startColor, allMemberCount), supportFragmentManager, currentFragment)
                                } else {
                                    renderFragment(InstructorClassActivityFragment.newInstance(classGroupId, startColor, allMemberCount), supportFragmentManager, currentFragment)
                                }
                            }
                            BottomNavigationId.ATTENDANCE.id -> renderFragment(StudentClassAttendanceFragment.newInstance(classGroupId), supportFragmentManager, currentFragment)
                            BottomNavigationId.STUDENT.id -> renderFragment(StudentClassMemberFragment.newInstance(classGroupId), supportFragmentManager, currentFragment)
                        }
                    }
                },
                onStudyPlanClicked = {
                    val isShowing = supportFragmentManager.findFragmentByTag(COURSE_SYLLABUS) != null
                    if (!isShowing) {
                        CourseSyllabusFragment.newInstance(classGroupId, currentTerm.id ,startColor, endColor).show(supportFragmentManager, COURSE_SYLLABUS)
                    }
                }
            )
        }
    }
}