package whiz.sspark.library.view.widget.collaboration.class_member

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
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

    fun init(member: ClassMember, position: Int, isSelf: Boolean) {
        with (member) {
            val color = if (colorCode.isNullOrBlank()) {
                Color.BLACK
            } else {
                colorCode.toColorInt()
            }

            binding.cvProfileImage.showClassMemberProfileCircle(profileImageUrl, abbreviatedName, Color.WHITE, color)

//            binding.tvNickname.text = resources.getString(R.string.class_member_number_place_holder, position, nickname)
            binding.tvName.text = if (isSelf) {
                convertToFullName(firstName, middleName, lastName)
            } else {
                convertToFullName(firstName, middleName, "${lastName.getFirstConsonant()}.")
            }
        }
    }
}