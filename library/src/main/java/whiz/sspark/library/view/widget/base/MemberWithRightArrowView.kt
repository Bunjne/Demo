package whiz.sspark.library.view.widget.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.MemberItem
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewMemberWithRightArrowBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle

class MemberWithRightArrowView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewMemberWithRightArrowBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(member: MemberItem,
             onMemberClicked: () -> Unit) {
        binding.ivArrow.show(R.drawable.ic_arrow_right)
        binding.ivProfile.showUserProfileCircle(member.imageUrl, getGender(member.gender).type)
        binding.tvName.text = member.name
        binding.tvDescription.text = member.description

        binding.root.setOnClickListener {
            onMemberClicked()
        }
    }
}