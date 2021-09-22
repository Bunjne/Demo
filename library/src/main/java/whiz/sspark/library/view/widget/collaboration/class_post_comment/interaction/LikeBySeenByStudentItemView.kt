package whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.databinding.ViewLikeBySeenByStudentItemBinding
import whiz.sspark.library.extension.showClassMemberProfileCircle
import whiz.sspark.library.extension.toColor

class LikeBySeenByStudentItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLikeBySeenByStudentItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(student: ClassMember, rank: Int) {
        with(student) {
            binding.ivProfile.showClassMemberProfileCircle(
                imageUrl = profileImageUrl,
                abbreviatedName = abbreviatedName,
                textColor = ContextCompat.getColor(context, R.color.naturalV100),
                textBackgroundColor = colorCode?.toColor() ?: Color.TRANSPARENT
            )

            binding.tvNickName.text = resources.getString(R.string.like_by_seen_by_name_with_rank, rank, nickname)
            binding.tvFullName.text = fullName
        }
    }
}