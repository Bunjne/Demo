package whiz.sspark.library.view.widget.collaboration.class_group.class_group_navigation_bar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.databinding.ViewClassGroupNavigationBarItemBinding
import whiz.sspark.library.extension.show

class ClassGroupNavigationBarItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassGroupNavigationBarItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(item: BottomNavigationBarItem,
             onItemClicked: () -> Unit) {
        if (item.imageResource != null) {
            binding.ivIcon.show(item.imageResource)
        } else {
            binding.ivIcon.show(item.imageUrl)
        }

        binding.tvTitle.text = item.title

        setOnClickListener {
            onItemClicked()
        }
    }
}