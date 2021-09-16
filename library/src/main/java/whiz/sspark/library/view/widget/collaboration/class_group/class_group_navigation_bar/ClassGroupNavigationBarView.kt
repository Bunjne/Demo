package whiz.sspark.library.view.widget.collaboration.class_group.class_group_navigation_bar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.databinding.ViewClassGroupNavigationBarBinding

class ClassGroupNavigationBarView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassGroupNavigationBarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(items: List<BottomNavigationBarItem>,
             onItemClicked: (Int) -> Unit) {
        with (binding.llClassGroupInfo) {
            removeAllViews()
            dividerDrawable = ContextCompat.getDrawable(context, R.drawable.divider_class_group_info).apply {
                alpha = 0.8f
            }

            items.forEach { item ->
                addView(ClassGroupNavigationBarItemView(context).apply {
                    init(item) {
                        onItemClicked(item.id)
                    }
                })
            }
        }
    }
}