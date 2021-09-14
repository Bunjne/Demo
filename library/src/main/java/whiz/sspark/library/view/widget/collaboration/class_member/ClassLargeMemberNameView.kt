package whiz.sspark.library.view.widget.collaboration.class_member

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import whiz.sspark.library.databinding.ViewClassLargeMemberNameBinding

class ClassLargeMemberNameView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassLargeMemberNameBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(name: String) {
        renderClassInstructorMemberNameView(name)
    }

    fun setNameColor(color: Int) {
        binding.tvName.setTextColor(color)
    }

    private fun renderClassInstructorMemberNameView(name: String) {
        binding.tvName.text = name
    }
}