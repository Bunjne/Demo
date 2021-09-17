package whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.databinding.ViewLikeBySeenByInstructorItemBinding
import whiz.sspark.library.extension.isDefaultValueContained
import whiz.sspark.library.extension.showClassMemberLikeBySeenByCircle
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.utility.convertToFullName

class LikeBySeenByInstructorItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLikeBySeenByInstructorItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(instructor: LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Instructor) {
        with(instructor.instructor) {
            binding.ivProfile.showClassMemberLikeBySeenByCircle(
                imageUrl = profileImageUrl,
                abbreviationName = abbreviatedName,
                textColor = ContextCompat.getColor(context, R.color.naturalV100),
                textBackgroundColor = colorCode?.toColor() ?: Color.TRANSPARENT
            )

            binding.tvFullName.text = fullName
            binding.tvPosition.text = remark
        }
    }
}