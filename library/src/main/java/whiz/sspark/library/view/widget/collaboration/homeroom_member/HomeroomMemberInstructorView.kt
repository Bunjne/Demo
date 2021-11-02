package whiz.sspark.library.view.widget.collaboration.homeroom_member

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewHomeroomMemberInstructorBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showProfile
import whiz.sspark.library.utility.convertToFullName

class HomeroomMemberInstructorView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewHomeroomMemberInstructorBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(member: ClassMember,
             isChatEnable: Boolean,
             onChatMemberClicked: (ClassMember) -> Unit) {
        binding.ivChat.show(R.drawable.ic_chat)

        with(member) {
            binding.cvProfileImage.showProfile(
                imageUrl = imageUrl,
                gender = getGender(gender).type
            )

            binding.tvName.text = convertToFullName(firstName, middleName, lastName, position)
            binding.tvDetail.text = jobPosition
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