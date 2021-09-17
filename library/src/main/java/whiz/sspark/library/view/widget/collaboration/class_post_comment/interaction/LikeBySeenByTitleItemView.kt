package whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewLikeBySeenByTitleItemBinding

class LikeBySeenByTitleItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLikeBySeenByTitleItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(header: LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Header) {
        binding.tvTitle.text = header.title
    }
}