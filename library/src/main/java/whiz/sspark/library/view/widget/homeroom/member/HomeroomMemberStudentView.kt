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
import whiz.sspark.library.databinding.ViewHomeroomMemberStudentBinding
import whiz.sspark.library.extension.getFirstConsonant
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showClassMemberProfileCircle
import whiz.sspark.library.utility.convertToFullName

class HomeroomMemberStudentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewHomeroomMemberStudentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(member: ClassMember,
             isSelf: Boolean,
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

            binding.tvNickname.text = if (number.isNullOrBlank()) {
                resources.getString(R.string.class_member_number_place_holder, member.code, nickname)
            } else {
                resources.getString(R.string.class_member_number_place_holder, member.number, nickname)
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