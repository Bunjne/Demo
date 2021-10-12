package whiz.tss.sspark.s_spark_android.presentation.homeroom.member.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Attendance
import whiz.sspark.library.data.entity.Member
import whiz.sspark.library.data.viewModel.ClassMemberViewModel
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.homeroom.member.HomeroomMemberAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentStudentHomeroomMemberBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.utility.retrieveUserID
import java.io.BufferedReader
import java.io.InputStreamReader

class StudentHomeroomMemberFragment : BaseFragment() {

    companion object {
        fun newInstance(classGroupId: String) = StudentHomeroomMemberFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
            }
        }
    }

    private var _binding: FragmentStudentHomeroomMemberBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClassMemberViewModel by viewModel()

    private val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    private val userId by lazy {
        retrieveUserID(requireContext())
    }

    val items = mutableListOf<HomeroomMemberAdapter.Item>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStudentHomeroomMemberBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            initView()

            viewModel.getClassMember(classGroupId, false)
        }
    }

    override fun initView() {
        binding.vHomeroomMember.init(
            items = items,
            onRefresh = {
                viewModel.getClassMember(classGroupId, true)
            },
            onChatMemberClicked = {

            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this, Observer { isLoading ->
            binding.vHomeroomMember.setSwipeRefreshLayout(isLoading)
        })
    }

    override fun observeData() {
        viewModel.memberResponse.observe(this, Observer {
            it?.let { member ->
                val instructorCounts = member.instructors.size
                val studentCounts = member.students.size

                val newItems = mutableListOf<HomeroomMemberAdapter.Item>()
                with(newItems) {
                    if (member.instructors.isNotEmpty()) {
                        val instructorTitle = HomeroomMemberAdapter.Item(
                            title = resources.getString(R.string.class_member_instructor_homeroom_title, instructorCounts)
                        )

                        add(instructorTitle)

                        val instructors = member.instructors.map { instructor ->
                            HomeroomMemberAdapter.Item(
                                instructor = instructor,
                                isChatEnable = true
                            )
                        }

                        addAll(instructors)
                    }

                    if (member.students.isNotEmpty()) {
                        val studentTitle = HomeroomMemberAdapter.Item(
                            title = resources.getString(R.string.class_member_student_title, studentCounts)
                        )

                        add(studentTitle)

                        val students = member.students.map { student ->
                            val isSelf = userId == student.id

                            HomeroomMemberAdapter.Item(
                                student = student,
                                isSelf = isSelf,
                                isChatEnable = false
                            )
                        }

                        addAll(students)
                    }
                }

                binding.vHomeroomMember.updateRecyclerViewItems(items, newItems)
            }
        })
    }

    override fun observeError() {
        viewModel.memberErrorResponse.observe(this, Observer {
            it?.let {
                showApiResponseXAlert(activity, it)
            }

            val reader = BufferedReader(InputStreamReader(requireContext().resources.assets.open("members.json")))
            val objects = reader.readText().toObject<Member>() ?: Member()

            val instructorCounts = objects.instructors.size
            val studentCounts = objects.students.size

            val newItems = mutableListOf<HomeroomMemberAdapter.Item>()
            with(newItems) {
                if (objects.instructors.isNotEmpty()) {
                    val instructorTitle = HomeroomMemberAdapter.Item(
                        title = resources.getString(R.string.class_member_instructor_homeroom_title, instructorCounts)
                    )

                    add(instructorTitle)

                    val instructors = objects.instructors.map { instructor ->
                        HomeroomMemberAdapter.Item(
                            instructor = instructor,
                            isChatEnable = true
                        )
                    }

                    addAll(instructors)
                }

                if (objects.students.isNotEmpty()) {
                    val studentTitle = HomeroomMemberAdapter.Item(
                        title = resources.getString(R.string.class_member_student_title, studentCounts)
                    )

                    add(studentTitle)

                    val students = objects.students.map { student ->
                        val isSelf = userId == student.id

                        HomeroomMemberAdapter.Item(
                            student = student,
                            isSelf = isSelf,
                            isChatEnable = false
                        )
                    }

                    addAll(students)
                }
            }

            binding.vHomeroomMember.updateRecyclerViewItems(items, newItems)
        })
    }
}
