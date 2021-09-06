package whiz.tss.sspark.s_spark_android.presentation.class_detail

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.data.enum.BottomNavigationType
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.data.enum.BottomNavigationId
import whiz.tss.sspark.s_spark_android.databinding.ActivityClassDetailBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class ClassDetailActivity : BaseActivity() {

    private val binding by lazy {
        ActivityClassDetailBinding.inflate(layoutInflater)
    }

    private val id by lazy {
        intent?.getStringExtra("id") ?: ""
    }

    private val color by lazy {
        intent?.getIntExtra("color", Color.BLACK) ?: Color.BLACK
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

    private var currentFragment: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun initView() {
//        with (vProfile) { TODO waiting for implement Profile Header
//            setBackgroundColor(color)
//            setTitleColor(Color.WHITE)
//            setSubTitleColor(Color.WHITE)
//            setVerticalBarColor(Color.WHITE)
//        }

        if (SSparkLibrary.isDarkModeEnabled) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.viewBaseSecondaryColor)
//            vProfile.background = ContextCompat.getDrawable(this, R.color.viewBaseSecondary)
//            vProfile.setVerticalBarColor(color)
        } else {
            window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_primary_gradient_0))

//            vProfile.setVerticalBarColor(ContextCompat.getColor(this, R.color.white))
        }

        val colors = intArrayOf(
            color,
            Color.GRAY
        )

        val bottomNavigationBarItems = mutableListOf(
            BottomNavigationBarItem(id = BottomNavigationId.ACTIVITY.id, title = resources.getString(R.string.class_detail_tab_activity), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_activity, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.ATTENDANCE.id, title = resources.getString(R.string.class_detail_tab_attendance), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_attendance, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.STUDENT.id, title = resources.getString(R.string.class_detail_tab_student), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_member, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.HOMEWORK.id, title = resources.getString(R.string.class_detail_tab_student), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_homework, colors = colors.toList())
        )

        with (binding.vClassDetail) {
            init(
                color = color,
                courseName = courseName,
                courseCode = courseCode,
                bottomNavigationBarItems =  bottomNavigationBarItems,
                onNavigationItemSelected = {
                    if (currentFragment != it) {
                        currentFragment = it
                        when (it) {
//                            BottomNavigationId.ACTIVITY.id -> renderFragment(ClassActivityFragment.newInstance(this@ClassDetailActivity.id as? String ?: "", color, allMemberCount), supportFragmentManager)
//                            BottomNavigationId.ATTENDANCE.id -> renderFragment(AttendanceClassFragment.newInstance(this@ClassDetailActivity.id as? String ?: "", color, courseCode, sectionNumber), supportFragmentManager)
//                            BottomNavigationId.MEMBER.id -> renderFragment(ClassMemberFragment.newInstance(this@ClassDetailActivity.id as? String ?: ""), supportFragmentManager)
                        }
                    }
                },
                onStudyPlanClicked = {
                    // TODO wait for activity navigation
                }
            )

            when (currentFragment) {
                BottomNavigationId.ACTIVITY.id -> {
                    renderFragment(ClassActivityFragment.newInstance(this@ClassDetailActivity.id, color, allMemberCount), supportFragmentManager, currentFragment)
                }
//                BottomNavigationId.ATTENDANCE.id -> {
//                    renderFragment(AttendanceClassFragment.newInstance(this@ClassDetailActivity.id, color, courseCode), supportFragmentManager, currentFragment)
//                }
//                BottomNavigationId.STUDENT.id -> {
//                    renderFragment(ClassMemberFragment.newInstance(this@ClassDetailActivity.id), supportFragmentManager, currentFragment)
//                }
                else -> { }
            }
        }
    }

    override fun observeView() {
        TODO("Not yet implemented")
    }

    override fun observeData() {
        TODO("Not yet implemented")
    }

    override fun observeError() {
        TODO("Not yet implemented")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("lastShowedFragment", currentFragment)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentFragment = savedInstanceState.getInt("lastShowedFragment")
    }
}