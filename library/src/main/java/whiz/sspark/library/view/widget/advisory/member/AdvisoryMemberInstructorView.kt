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
import whiz.sspark.library.databinding.ViewAdvisoryMemberInstructorBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showProfile
import whiz.sspark.library.utility.convertToFullName

class AdvisoryMemberInstructorView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAdvisoryMemberInstructorBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(member: ClassMember,
             isChatEnable: Boolean,
             onChatMemberClicked: (ClassMember) -> Unit) {
        binding.ivChat.show(R.drawable.ic_chat)

        with (member) {
            binding.cvProfileImage.showProfile(imageUrl, getGender(gender).type)

            binding.tvName.text = convertToFullName(firstName, middleName, lastName, position)
            binding.tvDetail.text = jobPosition
        }

        if (isChatEnable) {
            binding.ivChat.visibility = View.VISIBLE
        } else {
            binding.ivChat.visibility = View.GONE
            binding.ivChat.setOnClickListener {
                onChatMemberClicked(member)
            }
        }
    }
}