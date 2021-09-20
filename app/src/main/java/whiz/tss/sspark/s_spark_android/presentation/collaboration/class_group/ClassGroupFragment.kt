package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_group

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.data.entity.ClassGroup
import whiz.sspark.library.data.viewModel.ClassGroupViewModel
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.collaboration.class_group.ClassGroupAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.BottomNavigationId
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.databinding.FragmentClassGroupBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.ClassDetailActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class ClassGroupFragment : BaseFragment() {

    companion object {
        fun newInstance() = ClassGroupFragment()
    }

    private var _binding: FragmentClassGroupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClassGroupViewModel by viewModel()

    private val items = mutableListOf<ClassGroupAdapter.Item>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentClassGroupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            initView()

            viewModel.getClassGroups()
        }
    }

    override fun initView() {
        val firstNavigationItem = if (SSparkApp.role == RoleType.JUNIOR) {
            BottomNavigationBarItem(
                id = BottomNavigationId.HOMEROOM.id,
                title = resources.getString(R.string.class_group_navigation_item_homeroom_title),
                imageResource = R.drawable.ic_home
            )
        } else {
            BottomNavigationBarItem(
                id = BottomNavigationId.ADVISORY.id,
                title = resources.getString(R.string.class_group_navigation_item_advisory_title),
                imageResource = R.drawable.ic_home
            )
        }

        with (items) {
            add(
                ClassGroupAdapter.Item(
                    navigationBarItem = listOf(
                        firstNavigationItem,
                        BottomNavigationBarItem(
                            id = BottomNavigationId.ASSIGNMENT.id,
                            title = resources.getString(R.string.class_group_navigation_item_assignment_title),
                            imageResource = R.drawable.ic_homework
                        ),
                        BottomNavigationBarItem(
                            id = BottomNavigationId.CLASS_SCHEDULE.id,
                            title = resources.getString(R.string.class_group_navigation_item_class_schedule_title),
                            imageResource = R.drawable.ic_class_schedule
                        ),
                        BottomNavigationBarItem(
                            id = BottomNavigationId.EXAMINATION.id,
                            title = resources.getString(R.string.class_group_navigation_item_examination_title),
                            imageResource = R.drawable.ic_exam_schedule
                        )
                    )
                ))
        }

        with (binding.vClassGroup) {
            init(
                items = items,
                todayDate = Date(),
                classLevel = "มัธยมศึกษาปีที่ 3", //TODO change this value after discuss about source of class level
                schoolLogoUrl = "", //TODO change this value after discuss about source of schoolLogoUrl
                onClassGroupItemClicked = { classGroupCourse ->
                    val intent = Intent(requireContext(), ClassDetailActivity::class.java).apply {
                      putExtra("id", classGroupCourse.classGroupId)
                      putExtra("startColor", classGroupCourse.colorCode1.toColor())
                      putExtra("endColor", classGroupCourse.colorCode2.toColor())
                      putExtra("allMemberCount", classGroupCourse.studentCount)
                      putExtra("courseCode", classGroupCourse.courseCode)
                      putExtra("courseName", classGroupCourse.courseName)
                    }

                    startActivity(intent)
                },
                onNavigationBarItemClicked = { id ->
                    when (id) {
                        BottomNavigationId.ADVISORY.id -> {
                            //TODO wait for flow discussion
                        }
                        BottomNavigationId.HOMEROOM.id -> {
                            //TODO wait for flow discussion
                        }
                        BottomNavigationId.ASSIGNMENT.id -> {
                            //TODO wait for flow discussion
                        }
                        BottomNavigationId.CLASS_SCHEDULE.id -> {
                            //TODO wait for flow discussion
                        }
                        BottomNavigationId.EXAMINATION.id -> {
                            //TODO wait for flow discussion
                        }
                    }
                },
                onRefresh = {
                    viewModel.getClassGroups()
                }
            )
        }

        val reader = BufferedReader(InputStreamReader(requireContext().resources.assets.open("classgroup.json")))
        val objects = reader.readText().toObjects(Array<ClassGroup>::class.java)

        val classGroupItems = mutableListOf<ClassGroupAdapter.Item>()
        with (classGroupItems) {
            objects.forEach {
                add(ClassGroupAdapter.Item(
                    headerBarTitle = it.classGroupName,
                    headerBarIcon = it.iconImageUrl,
                    headerBarStartColor = it.colorCode1.toColor(),
                    headerBarEndColor = it.colorCode2.toColor()
                ))

                addAll(it.courses.map { classGroupCourse ->
                    ClassGroupAdapter.Item(
                        classGroupCourse = classGroupCourse
                    )
                })
            }
        }

        binding.vClassGroup.renderData(items, classGroupItems)
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this, Observer { isLoading ->
            binding.vClassGroup.setSwipeRefreshLayout(isLoading)
        })
    }

    override fun observeData() {
        viewModel.classGroupResponse.observe(this, Observer {
            it?.let { classGroups ->
                val classGroupItems = mutableListOf<ClassGroupAdapter.Item>()
                with (classGroupItems) {
                    classGroups.forEach {
                        add(ClassGroupAdapter.Item(
                            headerBarTitle = it.classGroupName,
                            headerBarIcon = it.iconImageUrl,
                            headerBarStartColor = it.colorCode1.toColor(),
                            headerBarEndColor = it.colorCode2.toColor()
                        ))

                        addAll(it.courses.map { classGroupCourse ->
                            ClassGroupAdapter.Item(
                                classGroupCourse = classGroupCourse
                            )
                        })
                    }
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
                requireContext().showAlertWithOkButton("")
            }
        })
    }
}