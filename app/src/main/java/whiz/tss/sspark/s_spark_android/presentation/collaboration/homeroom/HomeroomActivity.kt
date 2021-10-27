package whiz.tss.sspark.s_spark_android.presentation.collaboration.homeroom

import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.data.enum.BottomNavigationType
import whiz.sspark.library.utility.getHighSchoolLevel
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.BottomNavigationId
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.presentation.collaboration.ClassDetailActivity
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_activity.instructor.InstructorClassActivityFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_activity.student.StudentClassActivityFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_attendance.student.StudentClassAttendanceFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.homeroom.member.student.StudentHomeroomMemberFragment

class HomeroomActivity : ClassDetailActivity() {

    override val title by lazy {
        resources.getString(R.string.class_group_navigation_item_homeroom_title).uppercase()
    }

    override val subTitle by lazy {
        resources.getString(R.string.class_group_junior_class_level_place_holder,
            getHighSchoolLevel(currentTerm.academicGrade).toString(),
            currentTerm.roomNumber?.toString() ?: ""
        )
    }

    override val bottomNavigationBarItems by lazy {
        listOf(
            BottomNavigationBarItem(id = BottomNavigationId.ACTIVITY.id, title = resources.getString(R.string.class_detail_tab_activity), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_activity, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.ATTENDANCE.id, title = resources.getString(R.string.class_detail_tab_attendance), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_attendance, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.STUDENT.id, title = resources.getString(R.string.class_detail_tab_member), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_member, colors = colors.toList()),
        )
    }

    override fun initView() {
        super.initView()

        binding.vClassDetail.setStudyPlanVisibility(false)
    }

    override fun onNavigationItemSelected() {
        when (currentFragment) {
            BottomNavigationId.ACTIVITY.id -> {
                when (SSparkApp.role) {
                    RoleType.INSTRUCTOR_JUNIOR -> binding.vClassDetail.renderFragment(InstructorClassActivityFragment.newInstance(classGroupId, startColor, endColor, allMemberCount), supportFragmentManager, currentFragment)
                    RoleType.STUDENT_JUNIOR -> binding.vClassDetail.renderFragment(StudentClassActivityFragment.newInstance(classGroupId, startColor, endColor, allMemberCount), supportFragmentManager, currentFragment)
                }
            }
            BottomNavigationId.ATTENDANCE.id -> binding.vClassDetail.renderFragment(StudentClassAttendanceFragment.newInstance(classGroupId), supportFragmentManager, currentFragment)
            BottomNavigationId.STUDENT.id -> binding.vClassDetail.renderFragment(StudentHomeroomMemberFragment.newInstance(classGroupId), supportFragmentManager, currentFragment)
        }
    }
}