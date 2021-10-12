package whiz.sspark.library.view.widget.homeroom.member

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.databinding.ViewHomeroomMemberInstructorBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showClassMemberProfileCircle
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

        with (member) {
            val color = if (colorCode.isNullOrBlank()) {
                Color.BLACK
            } else {
                colorCode.toColorInt()
            }

            binding.cvProfileImage.showClassMemberProfileCircle(profileImageUrl, abbreviatedName, Color.WHITE, color)

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