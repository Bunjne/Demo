package whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.databinding.ViewLikeBySeenByClassMemberBinding

class LikeBySeenByClassMemberView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLikeBySeenByClassMemberBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(name: String, textColor: Int) {
        binding.tvName.setTextColor(textColor)
        binding.tvName.text = name
    }
}