package whiz.tss.sspark.s_spark_android.presentation.collaboration.homeroom.member.student

import android.os.Bundle
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_member.student.StudentClassMemberFragment

class StudentHomeroomMemberFragment : StudentClassMemberFragment() {

    companion object {
        fun newInstance(classGroupId: String) = StudentHomeroomMemberFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
            }
        }
    }

    override val isInstructorChatEnable = true

    override fun setMemberAdapter() {
        binding.vClassMember.setHomeroomMemberAdapter(
            items = items,
            onChatMemberClicked = {
                //TODO wait for Chat Screen implemented
            }
        )
    }

    override fun getInstructorTitle(instructorCount: Int) = resources.getString(R.string.class_member_instructor_homeroom_title, instructorCount)
}
