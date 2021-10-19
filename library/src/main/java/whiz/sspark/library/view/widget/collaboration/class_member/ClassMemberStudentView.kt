package whiz.sspark.library.view.widget.collaboration.class_member

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewClassMemberStudentBinding
import whiz.sspark.library.extension.getFirstConsonant
import whiz.sspark.library.extension.showProfile
import whiz.sspark.library.utility.convertToFullName

class ClassMemberStudentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassMemberStudentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(member: ClassMember, isSelf: Boolean) {
        with(member) {
            binding.cvProfileImage.showProfile(profileImageUrl, getGender(gender).type)

            binding.tvNickname.text = if (number != null) {
                resources.getString(R.string.class_member_number_place_holder, member.number.toString(), nickname)
            } else {
                resources.getString(R.string.class_member_number_place_holder, member.code, nickname)
            }

            binding.tvName.text = if (isSelf) {
                convertToFullName(firstName, middleName, lastName)
            } else {
                convertToFullName(firstName, middleName, "${lastName.getFirstConsonant()}.")
            }
        }
    }
}