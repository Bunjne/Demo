package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.MenuItem
import whiz.sspark.library.databinding.ViewMenuItemBinding
import whiz.sspark.library.extension.show

class MenuItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewMenuItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(menuItem: MenuItem) {
        binding.ivArrow.show(R.drawable.ic_arrow_right)
        binding.ivIcon.show(menuItem.imageUrl)
        binding.tvTitle.text = menuItem.name
    }
}