package whiz.sspark.library.view.widget.advisory.member

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewAdvisoryMemberStudentBinding
import whiz.sspark.library.extension.getFirstConsonant
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showProfile
import whiz.sspark.library.utility.convertToFullName

class AdvisoryMemberStudentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAdvisoryMemberStudentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(member: ClassMember,
             isSelf: Boolean,
             isChatEnable: Boolean,
             onChatMemberClicked: (ClassMember) -> Unit) {
        binding.ivChat.show(R.drawable.ic_chat)

        with(member) {
            binding.cvProfileImage.showProfile(profileImageUrl, getGender(gender).type)

            binding.tvNickname.text = if (number != null) {
                resources.getString(R.string.class_member_number_place_holder, member.code, nickname)
            } else {
                resources.getString(R.string.class_member_number_place_holder, member.number.toString(), nickname)
            }

            binding.tvName.text = if (isSelf) {
                convertToFullName(firstName, middleName, lastName)
            } else {
                convertToFullName(firstName, middleName, "${lastName.getFirstConsonant()}.")
            }
        }

        if (isChatEnable) {
            binding.ivChat.visibility = View.VISIBLE
            binding.ivChat.setOnClickListener {
                onChatMemberClicked(member)
            }
        } else {
            binding.ivChat.visibility = View.GONE
        }
    }
}