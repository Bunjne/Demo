package whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.LikeBySeenByMember
import whiz.sspark.library.databinding.ViewLikeBySeenByInstructorItemBinding
import whiz.sspark.library.extension.showClassMemberProfileCircle

class LikeBySeenByInstructorItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLikeBySeenByInstructorItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(instructor: LikeBySeenByMember) {
        with(instructor) {
            binding.ivProfile.showClassMemberProfileCircle(
                imageUrl = profileImageUrl,
                abbreviatedName = abbreviatedName,
                textColor = ContextCompat.getColor(context, R.color.naturalV100),
                textBackgroundColor = color
            )

            binding.tvFullName.text = fullName

            jobDescription?.let {
                binding.tvPosition.text = it
                binding.tvPosition.visibility = View.VISIBLE
            }
        }
    }
}