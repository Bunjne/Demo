package whiz.tss.sspark.s_spark_android.presentation.collaboration.advisory

import androidx.core.content.ContextCompat
import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.data.enum.BottomNavigationType
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.BottomNavigationId
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.presentation.collaboration.advisory.appointment.student.StudentAdvisoryAppointmentFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.advisory.member.AdvisoryMemberFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.ClassDetailActivity
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_activity.instructor.InstructorClassActivityFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_activity.student.StudentClassActivityFragment

class AdvisoryActivity : ClassDetailActivity() {

    override val bottomNavigationBarItems by lazy {
        listOf(
            BottomNavigationBarItem(id = BottomNavigationId.ACTIVITY.id, title = resources.getString(R.string.class_detail_tab_activity), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_activity, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.APPOINTMENT.id, title = resources.getString(R.string.advisory_appointment_tab_title), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_attendance, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.MEMBER.id, title = resources.getString(R.string.advisory_member_tab_title), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_member, colors = colors.toList()),
        )
    }

    private val appointmentSegmentTitles by lazy {
        listOf(
            resources.getString(R.string.advisory_appointment_pending_segment_title),
            resources.getString(R.string.advisory_appointment_past_segment_title)
        )
    }

    override fun initView() {
        super.initView()

        with(binding.vClassDetail) {
            initSegment(
                segmentTabTitles = appointmentSegmentTitles,
                textColorStateList = ContextCompat.getColorStateList(this@AdvisoryActivity, R.color.selector_text_advisory_segment),
                segmentBackgroundDrawable = ContextCompat.getDrawable(this@AdvisoryActivity, R.drawable.bg_advisory_appointment_segment),
                onSegmentTabClicked = { id ->
                    val targetFragment = (supportFragmentManager.findFragmentByTag(currentFragment.toString())) as? StudentAdvisoryAppointmentFragment

                    targetFragment?.let {
                        targetFragment.onSegmentChanged(id)
                    }
                }
            )

            setStudyPlanVisibility(false)
        }
    }

    override fun onNavigationItemSelected() {
        when (currentFragment) {
            BottomNavigationId.ACTIVITY.id -> {
                binding.vClassDetail.setSegmentVisibility(false)

                when (SSparkApp.role) {
                    RoleType.INSTRUCTOR_JUNIOR,
                    RoleType.INSTRUCTOR_SENIOR -> binding.vClassDetail.renderFragment(InstructorClassActivityFragment.newInstance(classGroupId, startColor, endColor, allMemberCount), supportFragmentManager, currentFragment)
                    RoleType.STUDENT_JUNIOR,
                    RoleType.STUDENT_SENIOR -> binding.vClassDetail.renderFragment(StudentClassActivityFragment.newInstance(classGroupId, startColor, endColor, allMemberCount), supportFragmentManager, currentFragment)
                    RoleType.GUARDIAN -> { } //TODO wait implement
                }
            }
            BottomNavigationId.APPOINTMENT.id -> {
                binding.vClassDetail.setSegmentVisibility(true)

                binding.vClassDetail.renderFragment(StudentAdvisoryAppointmentFragment.newInstance(), supportFragmentManager, currentFragment)
            }
            BottomNavigationId.MEMBER.id -> {
                binding.vClassDetail.setSegmentVisibility(false)

                binding.vClassDetail.renderFragment(AdvisoryMemberFragment.newInstance(classGroupId), supportFragmentManager, currentFragment)
            }
        }
    }
}