package whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.LikeBySeenByMember
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewLikeBySeenByStudentItemBinding
import whiz.sspark.library.extension.showProfile

class LikeBySeenByStudentItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLikeBySeenByStudentItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(student: LikeBySeenByMember) {
        with(student) {
            binding.ivProfile.showProfile(
                imageUrl = profileImageUrl,
                gender = getGender(gender).type
            )

            binding.tvNickName.text = title
            binding.tvFullName.text = fullName
        }
    }
}