package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_group

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.data.entity.ClassGroupCourse
import whiz.sspark.library.data.entity.ClassGroupDetail
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.viewModel.ClassGroupViewModel
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.utility.getHighSchoolLevel
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.collaboration.class_group.ClassGroupAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.BottomNavigationId
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.databinding.FragmentClassGroupBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.presentation.class_schedule.InstructorClassScheduleActivity
import whiz.tss.sspark.s_spark_android.presentation.class_schedule.StudentClassScheduleActivity
import whiz.tss.sspark.s_spark_android.presentation.assignment.AssignmentActivity
import whiz.tss.sspark.s_spark_android.presentation.assignment.InstructorAssignmentActivity
import whiz.tss.sspark.s_spark_android.presentation.collaboration.ClassDetailActivity
import whiz.tss.sspark.s_spark_android.presentation.exam_schedule.StudentExamScheduleActivity
import whiz.tss.sspark.s_spark_android.presentation.collaboration.homeroom.HomeroomActivity
import java.util.*

class ClassGroupFragment : BaseFragment() {

    companion object {
        fun newInstance() = ClassGroupFragment()
    }

    private var _binding: FragmentClassGroupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClassGroupViewModel by viewModel()

    private val items = mutableListOf<ClassGroupAdapter.Item>()
    private lateinit var currentTerm: Term
    private var specialClassGroup: ClassGroupCourse? = null
    private var guardianClassGroup: ClassGroupCourse? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentClassGroupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            lifecycleScope.launch {
                profileManager.term.collect {
                    it?.let {
                        currentTerm = it

                        initView()

                        viewModel.getClassGroups()
                    }
                }
            }
        }
    }

    override fun initView() {
        val navigationBarItems = mutableListOf<BottomNavigationBarItem>()
        when (SSparkApp.role) {
            RoleType.STUDENT_JUNIOR,
            RoleType.INSTRUCTOR_JUNIOR -> {
                navigationBarItems.add(
                    BottomNavigationBarItem(
                        id = BottomNavigationId.HOMEROOM.id,
                        title = resources.getString(R.string.class_group_navigation_item_homeroom_title),
                        imageResource = R.drawable.ic_home
                    )
                )
            }
            RoleType.STUDENT_SENIOR,
            RoleType.INSTRUCTOR_SENIOR -> {
                navigationBarItems.add(
                    BottomNavigationBarItem(
                        id = BottomNavigationId.ADVISORY.id,
                        title = resources.getString(R.string.class_group_navigation_item_advisory_title),
                        imageResource = R.drawable.ic_home
                    )
                )
            }
        }

        if (SSparkApp.role == RoleType.INSTRUCTOR_JUNIOR || SSparkApp.role == RoleType.INSTRUCTOR_SENIOR) {
            navigationBarItems.add(BottomNavigationBarItem(
                id = BottomNavigationId.GUARDIANS.id,
                title = resources.getString(R.string.class_group_navigation_item_guardians_title),
                imageResource = R.drawable.ic_homework
            ))
        }

        navigationBarItems.addAll(listOf(
            BottomNavigationBarItem(
                id = BottomNavigationId.ASSIGNMENT.id,
                title = resources.getString(R.string.class_group_navigation_item_assignment_title),
                imageResource = R.drawable.ic_homework
            ),
            BottomNavigationBarItem(
                id = BottomNavigationId.CLASS_SCHEDULE.id,
                title = resources.getString(R.string.class_group_navigation_item_class_schedule_title),
                imageResource = R.drawable.ic_class_schedule
            )
        ))

        items.add(ClassGroupAdapter.Item(navigationBarItems = navigationBarItems))

        val classLevel = if (SSparkApp.role == RoleType.STUDENT_JUNIOR) {
            resources.getString(R.string.class_group_junior_class_level_place_holder, getHighSchoolLevel(currentTerm.academicGrade).toString(), currentTerm.roomNumber?.toString() ?: "")
        } else {
            resources.getString(R.string.class_group_senior_class_level_place_holder, getHighSchoolLevel(currentTerm.academicGrade).toString())
        }

        with (binding.vClassGroup) {
            init(
                items = items,
                todayDate = Date(),
                classLevel = classLevel,
                schoolLogoUrl = "", //TODO change this value after discuss about source of schoolLogoUrl
                onClassGroupItemClicked = { classGroupCourse ->
                    val intent = Intent(requireContext(), ClassDetailActivity::class.java).apply {
                      putExtra("classGroupId", classGroupCourse.classGroupId)
                      putExtra("startColor", classGroupCourse.colorCode1.toColor())
                      putExtra("endColor", classGroupCourse.colorCode2.toColor())
                      putExtra("allMemberCount", classGroupCourse.memberCount)
                      putExtra("title", classGroupCourse.courseCode)
                      putExtra("subTitle", classGroupCourse.courseName)
                    }

                    startActivity(intent)
                },
                onNavigationBarItemClicked = { id ->
                    when (id) {
                        BottomNavigationId.ADVISORY.id -> {
                            //TODO wait for implementation
                        }
                        BottomNavigationId.HOMEROOM.id -> {
                            val intent = Intent(requireContext(), HomeroomActivity::class.java).apply {
                                putExtra("classGroupId", specialClassGroup?.classGroupId)
                                putExtra("allMemberCount", specialClassGroup?.memberCount)
                                putExtra("title", specialClassGroup?.courseCode)
                                putExtra("subTitle", specialClassGroup?.courseName)
                            }

                            startActivity(intent)
                        }
                        BottomNavigationId.ASSIGNMENT.id -> {
                            when (SSparkApp.role) {
                                RoleType.INSTRUCTOR_SENIOR,
                                RoleType.INSTRUCTOR_JUNIOR -> {
                                    val intent = Intent(requireContext(), InstructorAssignmentActivity::class.java)
                                    startActivity(intent)
                                }
                                RoleType.STUDENT_SENIOR,
                                RoleType.STUDENT_JUNIOR -> {
                                    val intent = Intent(requireContext(), AssignmentActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                        BottomNavigationId.CLASS_SCHEDULE.id -> {
                            when (SSparkApp.role) {
                                RoleType.INSTRUCTOR_SENIOR,
                                RoleType.INSTRUCTOR_JUNIOR -> {
                                    val intent = Intent(requireContext(), InstructorClassScheduleActivity::class.java)
                                    startActivity(intent)
                                }
                                RoleType.STUDENT_SENIOR,
                                RoleType.STUDENT_JUNIOR -> {
                                    val intent = Intent(requireContext(), StudentClassScheduleActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                        BottomNavigationId.EXAMINATION.id -> {
                            val intent = Intent(requireContext(), StudentExamScheduleActivity::class.java)
                            startActivity(intent)
                        }
                    }
                },
                onRefresh = {
                    viewModel.getClassGroups()
                }
            )
        }
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this, Observer { isLoading ->
            binding.vClassGroup.setSwipeRefreshLayout(isLoading)
        })
    }

    override fun observeData() {
        viewModel.classGroupResponse.observe(this, Observer {
            it?.let { classGroup ->
                specialClassGroup = classGroup.specialGroup
                guardianClassGroup = classGroup.guardianGroup

                val classGroupItems = if (SSparkApp.role == RoleType.STUDENT_JUNIOR) {
                    transformData(classGroup.juniorClasses)
                } else {
                    transformData(classGroup.seniorClasses)
                }

                binding.vClassGroup.renderData(items, classGroupItems)
            }
        })
    }

    override fun observeError() {
        viewModel.classGroupErrorResponse.observe(this, Observer {
            it?.let {
                showApiResponseXAlert(activity, it)
            }

            binding.vClassGroup.setNoClassGroupVisibility(true)
        })

        viewModel.errorMessage.observe(this, Observer {
            it?.let {
                requireContext().showAlertWithOkButton(it)
            }
        })
    }

    private fun transformData(classGroups: List<ClassGroupDetail>): MutableList<ClassGroupAdapter.Item> {
        val classGroupItems = mutableListOf<ClassGroupAdapter.Item>()
        with (classGroupItems) {
            classGroups.forEach {
                add(ClassGroupAdapter.Item(
                    headerBarTitle = it.classGroupName,
                    headerBarIcon = it.iconImageUrl,
                    gradientColor = it.gradientColors
                ))

                val classGroupCourseItems = it.courses.map { classGroupCourse ->
                    ClassGroupAdapter.Item(classGroupCourse = classGroupCourse)
                }

                addAll(classGroupCourseItems)
            }
        }

        return classGroupItems
    }
}