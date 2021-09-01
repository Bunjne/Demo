package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.MenuMember
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewMemberWithRightArrowBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle

class MenuMemberView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewMemberWithRightArrowBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(menuMember: MenuMember,
             onMemberClicked: (MenuMember) -> Unit) {
        binding.ivArrow.show(R.drawable.ic_arrow_right)
        binding.ivProfile.showUserProfileCircle(menuMember.imageUrl, getGender(menuMember.gender).type)
        binding.tvName.text = menuMember.name
        binding.tvDescription.text = menuMember.description

        binding.root.setOnClickListener {
            onMemberClicked(menuMember)
        }
    }
}