package whiz.tss.sspark.s_spark_android.presentation.collaboration

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.BottomNavigationBarItem
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
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_post_comment.interaction.LikeBySeenByDialogFragment

class ClassDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityClassDetailBinding

    private val id by lazy {
        intent?.getStringExtra("id") ?: ""
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initView()

        LikeBySeenByDialogFragment.newInstance("","",0, 0, 1).show(supportFragmentManager, "")
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
            BottomNavigationBarItem(id = BottomNavigationId.HOMEWORK.id, title = resources.getString(R.string.class_detail_tab_homework), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_homework, colors = colors.toList())
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
                                    renderFragment(StudentClassActivityFragment.newInstance(this@ClassDetailActivity.id, startColor, allMemberCount), supportFragmentManager, currentFragment)
                                } else {
                                    renderFragment(InstructorClassActivityFragment.newInstance(this@ClassDetailActivity.id, startColor, allMemberCount), supportFragmentManager, currentFragment)
                                }
                            }
//                            BottomNavigationId.ATTENDANCE.id -> renderFragment(AttendanceClassFragment.newInstance(this@ClassDetailActivity.id, color, courseCode, sectionNumber), supportFragmentManager)
//                            BottomNavigationId.MEMBER.id -> renderFragment(ClassMemberFragment.newInstance(this@ClassDetailActivity.id), supportFragmentManager)
                        }
                    }
                },
                onStudyPlanClicked = {
                    // TODO wait for activity navigation
                }
            )
        }
    }
}