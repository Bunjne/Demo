package whiz.sspark.library.view.widget.collaboration.class_member

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewClassMemberStudentBinding
import whiz.sspark.library.extension.getFirstConsonant
import whiz.sspark.library.extension.showClassMemberProfileCircle
import whiz.sspark.library.utility.convertToFullName

class ClassMemberStudentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassMemberStudentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(item: ClassMemberAdapter.Item, position: Int, isSelf: Boolean) {
        val member = item.instructor ?: item.student
        member?.let { member ->
            val color = if (member.colorCode.isNullOrBlank()) {
                Color.BLACK
            } else {
                member.colorCode.toColorInt()
            }

            binding.cvProfileImage.showClassMemberProfileCircle(member.profileImageUrl, member, Color.WHITE, color)

            binding.tvNickname.text = resources.getString(R.string.class_member_number_place_holder, position, member.nickname)
            binding.tvName.text = if (isSelf) {
                convertToFullName(member.firstName, member.middleName, member.lastName)
            } else {
                convertToFullName(member.firstName, member.middleName, "${member.lastName.getFirstConsonant()}.")
            }
        }
    }
}