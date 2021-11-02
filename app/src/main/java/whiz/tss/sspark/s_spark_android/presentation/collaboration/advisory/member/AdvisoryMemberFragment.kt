package whiz.tss.sspark.s_spark_android.presentation.collaboration.advisory.member

import android.os.Bundle
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_member.student.StudentClassMemberFragment

class AdvisoryMemberFragment : StudentClassMemberFragment() {

    companion object {
        fun newInstance(classGroupId: String) = AdvisoryMemberFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
            }
        }
    }

    override val isInstructorChatEnable = true

    override fun initView() {
        super.initView()

        binding.vClassMember.setAdvisoryMemberAdapter(
            items = items,
            onChatMemberClicked = {
                //TODO wait for Chat Screen implemented
            }
        )
    }

    override fun getInstructorTitle(instructorCount: Int) = resources.getString(R.string.advisory_member_instructor_title, instructorCount)
}
