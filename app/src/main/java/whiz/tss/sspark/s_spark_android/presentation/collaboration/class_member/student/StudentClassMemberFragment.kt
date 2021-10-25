package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_member.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.viewModel.ClassMemberViewModel
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.collaboration.class_member.ClassMemberAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentStudentClassMemberBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.utility.retrieveUserID

class StudentClassMemberFragment : BaseFragment() {

    companion object {
        fun newInstance(classGroupId: String) = StudentClassMemberFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
            }
        }
    }

    private var _binding: FragmentStudentClassMemberBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClassMemberViewModel by viewModel()

    private val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    private val userId by lazy {
        retrieveUserID(requireContext())
    }

    val items = mutableListOf<ClassMemberAdapter.Item>()

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
            items = items,
            onRefresh = {
                viewModel.getClassMember(classGroupId, true)
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this, Observer { isLoading ->
            binding.vClassMember.setSwipeRefreshLayout(isLoading)
        })
    }

    override fun observeData() {
        viewModel.memberResponse.observe(this, Observer {
            it?.let { member ->
                val instructorCounts = member.instructors.size
                val studentCounts = member.students.size

                val newItems = mutableListOf<ClassMemberAdapter.Item>()
                with(newItems) {
                    if (member.instructors.isNotEmpty()) {
                        add(ClassMemberAdapter.Item(
                            title = resources.getString(R.string.class_member_instructor_title, instructorCounts)
                        ))

                        addAll(member.instructors.map { instructor ->
                            ClassMemberAdapter.Item(
                                instructor = instructor
                            )
                        })
                    }

                    if (member.students.isNotEmpty()) {
                        add(ClassMemberAdapter.Item(
                            title = resources.getString(R.string.class_member_student_title, studentCounts)
                        ))

                        addAll(member.students.map { student ->
                            val isSelf = userId == student.id

                            ClassMemberAdapter.Item(
                                title = null,
                                student = student,
                                instructor = null,
                                isSelf = isSelf
                            )
                        })
                    }
                }

                binding.vClassMember.updateRecyclerViewItems(items, newItems)
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
}
