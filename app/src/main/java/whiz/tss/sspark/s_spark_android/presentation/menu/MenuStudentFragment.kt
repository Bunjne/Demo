package whiz.tss.sspark.s_spark_android.presentation.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.MenuMember
import whiz.sspark.library.data.entity.MenuSegment
import whiz.sspark.library.data.entity.Student
import whiz.sspark.library.data.enum.MenuSegmentType
import whiz.sspark.library.data.enum.getGender
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.databinding.FragmentMenuBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment

class MenuStudentFragment : BaseFragment() {

    companion object {
        fun newInstance() = MenuStudentFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private var student: Student = Student()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            profileManager.student.collect {
                it?.let {
                    student = it
                }
            }
        }

        initView()
    }

    override fun initView() {
        val segments = if (SSparkApp.role == RoleType.JUNIOR) {
            listOf(MenuSegment(resources.getString(R.string.menu_junior_segment_instructor), MenuSegmentType.INSTRUCTOR),
                MenuSegment(resources.getString(R.string.menu_segment_guardian), MenuSegmentType.GUARDIAN))
        } else {
            listOf(MenuSegment(resources.getString(R.string.menu_senior_segment_instructor), MenuSegmentType.INSTRUCTOR),
                MenuSegment(resources.getString(R.string.menu_segment_guardian), MenuSegmentType.GUARDIAN))
        }

        binding.vMenu.init(
            student = student,
            segments = segments,
            onCameraClicked = {

            },
            onMemberClicked = {

            },
            onRefresh = {
                binding.vMenu.updateMember(listOf(
                    MenuMember(MenuSegmentType.INSTRUCTOR, "", "1", "test2", "name5"),
                    MenuMember(MenuSegmentType.INSTRUCTOR, "", "2", "test3", "name6"),
                    MenuMember(MenuSegmentType.GUARDIAN, "", "1", "test1", "name7"),
                    MenuMember(MenuSegmentType.INSTRUCTOR, "", "1", "test1", "name4")))
            }
        )

        binding.vMenu.updateMember(listOf(
            MenuMember(MenuSegmentType.INSTRUCTOR, "", "1", "test1", "name1"),
            MenuMember(MenuSegmentType.GUARDIAN, "", "2", "test1", "name2"),
            MenuMember(MenuSegmentType.GUARDIAN, "", "1", "test1", "name3"),
            MenuMember(MenuSegmentType.INSTRUCTOR, "", "1", "test1", "name4")))
    }

    override fun observeView() {

    }

    override fun observeData() {
        profileManager.student.asLiveData().observe(this) {
            it?.let {
                student = it
                binding.vMenu.updateStudentProfileImage(student.profileImageUrl, getGender(it.gender)?.type)
            }
        }
    }

    override fun observeError() {

    }
}