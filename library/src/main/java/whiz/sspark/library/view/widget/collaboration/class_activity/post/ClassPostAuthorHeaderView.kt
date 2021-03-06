package whiz.sspark.library.view.widget.collaboration.class_activity.post

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewClassPostAuthorHeaderBinding
import whiz.sspark.library.extension.*
import whiz.sspark.library.utility.convertToFullName
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

            binding.cvProfile.showProfile(it.imageUrl, getGender(it.gender).type)

            binding.tvName.text = convertToFullName(it.firstName, it.middleName, it.lastName, it.position).toUpperCase()
            binding.tvDate.text = if (createdAt == updatedAt) {
                updatedAt.toLocalDate()?.toPostTime(context)
            } else {
                resources.getString(R.string.class_post_updated_at, updatedAt.toLocalDate()?.toPostTime(context))
            }
        }

        with(binding.cvNew) {
            visibility = if (isRead) View.GONE else View.VISIBLE
            setCardBackgroundColor(color)
        }
    }
}