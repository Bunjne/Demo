package whiz.sspark.library.view.widget.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Advisee
import whiz.sspark.library.data.entity.MemberItem
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewAdviseeProfileBinding
import whiz.sspark.library.databinding.ViewMemberWithRightArrowBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle

class AdviseeProfileView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAdviseeProfileBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(advisee: Advisee) {
        with(advisee) {
            binding.ivProfile.showUserProfileCircle(imageUrl, getGender(gender).type)
            binding.tvCodeAndNickname.text = resources.getString(R.string.general_code_and_nickname_placeholder, code, nickname)
            binding.tvFullname.text = name
        }
    }
}