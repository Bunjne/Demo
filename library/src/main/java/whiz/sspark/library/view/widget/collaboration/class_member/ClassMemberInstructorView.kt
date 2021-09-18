package whiz.sspark.library.view.widget.collaboration.class_member

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import whiz.sspark.library.data.entity.ClassMember
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

    fun init(member: ClassMember) {
        with (member) {
            val color = if (colorCode.isNullOrBlank()) {
                Color.BLACK
            } else {
                colorCode.toColorInt()
            }

            binding.cvProfileImage.showClassMemberProfileCircle(profileImageUrl, this, Color.WHITE, color)

            binding.tvName.text = convertToFullName(firstName, middleName, lastName, position)
            binding.tvDetail.text = remark
        }
    }
}