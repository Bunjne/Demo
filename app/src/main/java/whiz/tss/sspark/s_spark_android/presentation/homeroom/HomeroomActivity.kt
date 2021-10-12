package whiz.tss.sspark.s_spark_android.presentation.homeroom

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
import whiz.sspark.library.utility.getHighSchoolLevel
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.BottomNavigationId
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.databinding.ActivityHomeroomBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_attendance.student.StudentClassAttendanceFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_member.student.StudentClassMemberFragment
import whiz.tss.sspark.s_spark_android.presentation.homeroom.activity.student.StudentHomeroomActivityFragment
import whiz.tss.sspark.s_spark_android.presentation.homeroom.attendance.student.StudentHomeroomAttendanceFragment
import whiz.tss.sspark.s_spark_android.presentation.homeroom.member.student.StudentHomeroomMemberFragment

class HomeroomActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeroomBinding

    companion object {
        const val HOMEROOM_CLASS_GROUP_ID = "Homeroom" //TODO wait for confirmation
    }

    private val startColor by lazy {
        ContextCompat.getColor(this, R.color.primaryStartColor)
    }

    private val endColor by lazy {
        ContextCompat.getColor(this, R.color.primaryEndColor)
    }

    private val allMemberCount by lazy {
        intent?.getIntExtra("allMemberCount", 0) ?: 0
    }

    private var currentFragment: Int = BottomNavigationId.NONE_SELECTED.id
    private lateinit var currentTerm: Term

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeroomBinding.inflate(layoutInflater)
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
                window.statusBarColor = ContextCompat.getColor(this@HomeroomActivity, R.color.viewBaseSecondaryColor) //TODO change this line when darkmode is enable
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
        )

        with(binding.vHomeroom) {
            init(
                backgroundDrawable = gradientDrawable,
                level = getHighSchoolLevel(currentTerm.academicGrade).toString(),
                room = currentTerm.roomNumber?.toString() ?: "",
                bottomNavigationBarItems =  bottomNavigationBarItems,
                onNavigationItemSelected = {
                    if (currentFragment != it) {
                        currentFragment = it
                        when (it) {
                            BottomNavigationId.ACTIVITY.id -> {
                                when (SSparkApp.role) {
                                    RoleType.INSTRUCTOR_JUNIOR -> {} //TODO wait for Instructor Homeroom Screen Implement
                                    RoleType.STUDENT_JUNIOR -> renderFragment(StudentHomeroomActivityFragment.newInstance(HOMEROOM_CLASS_GROUP_ID, startColor, allMemberCount), supportFragmentManager, currentFragment)
                                }
                            }
                            BottomNavigationId.ATTENDANCE.id -> renderFragment(StudentHomeroomAttendanceFragment.newInstance(HOMEROOM_CLASS_GROUP_ID), supportFragmentManager, currentFragment)
                            BottomNavigationId.STUDENT.id -> renderFragment(StudentHomeroomMemberFragment.newInstance(HOMEROOM_CLASS_GROUP_ID), supportFragmentManager, currentFragment)
                        }
                    }
                }
            )
        }
    }
}