package whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.databinding.ViewLikeBySeenByBinding
import whiz.sspark.library.view.general.custom_divider.CustomDividerItemDecoration

class LikeBySeenByDialogFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLikeBySeenByBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val items = mutableListOf<LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType>()

    fun init(startColor: Int, endColor: Int, onCloseClicked: () -> Unit) {
        val gradientDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
            colors = intArrayOf(startColor, endColor)
        }

        binding.clHeader.background = gradientDrawable
        binding.tvClose.setOnClickListener {
            onCloseClicked()
        }

        with(binding.rvMemberLikedOrSeen) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(CustomDividerItemDecoration(ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!, 1))

            adapter = LikeBySeenByItemAdapter(
                context = context,
                items = items
            )
        }
    }

//    fun renderMembers(members: List<ClassMember>)
}