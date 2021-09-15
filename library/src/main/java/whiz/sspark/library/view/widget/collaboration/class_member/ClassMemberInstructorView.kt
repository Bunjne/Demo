package whiz.sspark.library.view.widget.collaboration.class_member

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import whiz.sspark.library.databinding.ViewClassMemberInstructorBinding
import whiz.sspark.library.extension.showClassMemberProfileCircle
import whiz.sspark.library.utility.convertToFullName

class ClassMemberInstructorView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassMemberInstructorBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(item: ClassMemberAdapter.Item) {
        val member = item.instructor ?: item.student
        member?.let { member ->
            val color = if (member.colorCode.isNullOrBlank()) {
                Color.BLACK
            } else {
                member.colorCode.toColorInt()
            }

            binding.cvProfileImage.showClassMemberProfileCircle(member.profileImageUrl, member, Color.WHITE, color)

            binding.tvName.text = convertToFullName(member.firstName, member.middleName, member.lastName, member.position)
            binding.tvDetail.text = member.remark
        }
    }
}