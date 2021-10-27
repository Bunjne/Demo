package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_member.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.ClassMemberItem
import whiz.sspark.library.data.entity.Member
import whiz.sspark.library.data.viewModel.ClassMemberViewModel
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentStudentClassMemberBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment

open class StudentClassMemberFragment : BaseFragment() {

    companion object {
        fun newInstance(classGroupId: String) = StudentClassMemberFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
            }
        }
    }

    private var _binding: FragmentStudentClassMemberBinding? = null
    protected val binding get() = _binding!!

    private val viewModel: ClassMemberViewModel by viewModel()

    private val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    protected val items = mutableListOf<ClassMemberItem>()

    protected open val isChatEnable = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStudentClassMemberBinding.inflate(layoutInflater)
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
        binding.vClassMember.init(
            onRefresh = {
                viewModel.getClassMember(classGroupId, true)
            }
        )

        binding.vClassMember.setClassMemberAdapter(items)
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this, Observer { isLoading ->
            binding.vClassMember.setSwipeRefreshLayout(isLoading)
        })
    }

    override fun observeData() {
        viewModel.memberResponse.observe(this, Observer {
            it?.let { member ->
                renderData(member)
            }
        })
    }

    override fun observeError() {
        viewModel.memberErrorResponse.observe(this, Observer {
            it?.let {
                showApiResponseXAlert(activity, it)
            }
        })
    }

    private fun renderData(member: Member) {
        val instructorCounts = member.instructors.size
        val studentCounts = member.students.size

        val newItems = mutableListOf<ClassMemberItem>()
        with(newItems) {
            if (member.instructors.isNotEmpty()) {
                val instructorTitle = ClassMemberItem(
                    title = getInstructorTitle(instructorCounts)
                )

                add(instructorTitle)

                val instructors = member.instructors.map { instructor ->
                    ClassMemberItem(
                        instructor = instructor,
                        isChatEnable = isChatEnable
                    )
                }

                addAll(instructors)
            }

            if (member.students.isNotEmpty()) {
                val studentTitle = ClassMemberItem(
                    title = resources.getString(R.string.class_member_student_title, studentCounts)
                )

                add(studentTitle)

                val students = member.students.map { student ->
                    ClassMemberItem(
                        student = student,
                        isChatEnable = !isChatEnable
                    )
                }

                addAll(students)
            }
        }

        binding.vClassMember.updateMemberRecyclerViewItems(items, newItems)
    }

    protected open fun getInstructorTitle(instructorCount: Int) = resources.getString(R.string.class_member_instructor_title, instructorCount)
}
