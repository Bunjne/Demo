package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewMenuContactItemBinding
import whiz.sspark.library.extension.show

class MenuContactItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewMenuContactItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String,
             onContactClicked: () -> Unit) {
        binding.tvTitle.text = title
        binding.ivEdit.show(R.drawable.ic_create_post)
        binding.cvContactInfo.setOnClickListener {
            onContactClicked()
        }
    }
}