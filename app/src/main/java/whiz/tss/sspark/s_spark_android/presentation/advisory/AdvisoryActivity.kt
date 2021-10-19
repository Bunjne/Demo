package whiz.tss.sspark.s_spark_android.presentation.advisory

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
import whiz.tss.sspark.s_spark_android.databinding.ActivityAdvisoryBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.advisory.appointment.student.StudentAdvisoryAppointmentFragment
import whiz.tss.sspark.s_spark_android.presentation.advisory.member.AdvisoryMemberFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_activity.instructor.InstructorClassActivityFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_activity.student.StudentClassActivityFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_attendance.student.StudentClassAttendanceFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_member.student.StudentClassMemberFragment

class AdvisoryActivity : BaseActivity() {

    private lateinit var binding: ActivityAdvisoryBinding

    private val classGroupId by lazy {
        intent?.getStringExtra("classGroupId") ?: ""
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvisoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
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
                window.statusBarColor = ContextCompat.getColor(this@AdvisoryActivity, R.color.viewBaseSecondaryColor) //TODO change this line when darkmode is enable
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
            BottomNavigationBarItem(id = BottomNavigationId.APPOINTMENT.id, title = resources.getString(R.string.advisory_appointment_tab_title), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_appointment, colors = colors.toList()),
            BottomNavigationBarItem(id = BottomNavigationId.STUDENT.id, title = resources.getString(R.string.advisory_member_tab_title), type = BottomNavigationType.CLASS_COLLABORATION.id, imageResource = R.drawable.ic_member, colors = colors.toList())
        )

        val appointmentSegmentTitles = listOf(
            resources.getString(R.string.advisory_appointment_pending_segment_title),
            resources.getString(R.string.advisory_appointment_past_segment_title)
        )

        with(binding.vAdvisory) {
            init(
                backgroundHeaderDrawable = gradientDrawable,
                bottomNavigationBarItems =  bottomNavigationBarItems,
                segmentTabTitles = appointmentSegmentTitles,
                textColorStateList = ContextCompat.getColorStateList(this@AdvisoryActivity, R.color.selector_text_advisory_segment),
                segmentBackgroundDrawable = ContextCompat.getDrawable(this@AdvisoryActivity, R.drawable.bg_advisory_appointment_segment),
                onSegmentTabClicked = { id ->
                    val targetFragment = (supportFragmentManager.findFragmentByTag(currentFragment.toString())) as? StudentAdvisoryAppointmentFragment

                    targetFragment?.let {
                        targetFragment.onSegmentChanged(id)
                    }
                },
                onNavigationItemSelected = {
                    if (currentFragment != it) {
                        currentFragment = it
                        when (it) {
                            BottomNavigationId.ACTIVITY.id -> {
                                setSegmentVisibility(false)

                                when (SSparkApp.role) {
                                    RoleType.INSTRUCTOR_JUNIOR,
                                    RoleType.INSTRUCTOR_SENIOR -> renderFragment(InstructorClassActivityFragment.newInstance(classGroupId, startColor, allMemberCount), supportFragmentManager, currentFragment)
                                    RoleType.STUDENT_JUNIOR,
                                    RoleType.STUDENT_SENIOR -> renderFragment(StudentClassActivityFragment.newInstance(classGroupId, startColor, allMemberCount), supportFragmentManager, currentFragment)
                                    RoleType.GUARDIAN -> { } //TODO wait implement
                                }
                            }
                            BottomNavigationId.APPOINTMENT.id -> {
                                setSegmentVisibility(true)

                                renderFragment(StudentAdvisoryAppointmentFragment.newInstance(), supportFragmentManager, currentFragment)
                            }
                            BottomNavigationId.STUDENT.id -> {
                                setSegmentVisibility(false)
                                
                                renderFragment(AdvisoryMemberFragment.newInstance(classGroupId), supportFragmentManager, currentFragment)
                            }
                        }
                    }
                }
            )
        }
    }
}