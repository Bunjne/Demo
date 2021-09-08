package whiz.sspark.library.view.widget.collaboration.class_activity.post

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.databinding.ViewClassPostAuthorHeaderBinding
import whiz.sspark.library.extension.showClassMemberProfileCircle
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.extension.toInstructorFullName
import whiz.sspark.library.extension.toPostTime
import java.util.*

class ClassPostAuthorHeaderView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassPostAuthorHeaderBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(member: ClassMember?, createdAt: Date, updatedAt: Date, isRead: Boolean, color: Int) {
        member?.let {
            val memberColor = if (it.colorCode.isNullOrBlank()) {
                Color.BLACK
            } else {
                it.colorCode.toColor()
            }

            binding.cvProfile.showClassMemberProfileCircle(it.profileImageUrl, it, Color.WHITE, memberColor)

            binding.tvName.text = it.toInstructorFullName().toUpperCase()
            binding.tvDate.text = if (createdAt == updatedAt) {
                updatedAt.toPostTime(context)
            } else {
                resources.getString(R.string.class_post_updated_at, updatedAt.toPostTime(context))
            }
        }

        with(binding.cvNew) {
            visibility = if (isRead) View.GONE else View.VISIBLE
            setCardBackgroundColor(color)
        }
    }
}