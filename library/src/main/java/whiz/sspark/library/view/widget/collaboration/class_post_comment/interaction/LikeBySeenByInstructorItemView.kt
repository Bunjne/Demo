package whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.LikeBySeenByMember
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewLikeBySeenByInstructorItemBinding
import whiz.sspark.library.extension.showProfile

class LikeBySeenByInstructorItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLikeBySeenByInstructorItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(instructor: LikeBySeenByMember) {
        with(instructor) {
            binding.ivProfile.showProfile(
                imageUrl = profileImageUrl,
                gender = getGender(gender).type
            )

            binding.tvFullName.text = fullName

            jobDescription?.let {
                binding.tvPosition.text = it
                binding.tvPosition.visibility = View.VISIBLE
            }
        }
    }
}