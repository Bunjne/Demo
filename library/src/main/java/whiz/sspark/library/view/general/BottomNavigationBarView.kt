package whiz.sspark.library.view.general

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEachIndexed
import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.databinding.ViewBottomNavigationBarBinding

class BottomNavigationBarView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewBottomNavigationBarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(context: Context,
             imageList: List<BottomNavigationBarItem>,
             onSelected: (Int) -> Unit) {
        with(binding.llNavigation) {
            removeAllViews()
            var isChecked = false
            imageList.forEachIndexed { index, item ->
                addView(BottomNavigationBarItemView(context).apply {
                    val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                    param.weight = 1f
                    layoutParams = param
                    id = item.id
                    init(context, item) {
                        setSelection(it)
                        onSelected(it)
                    }

                    if(!isChecked) {
                        setSelectedImage()
                        setSelectedText()
                        onSelected(item.id)
                        isChecked = true
                    } else {
                        setUnSelectedImage()
                        setUnselectedText()
                    }
                })
            }
        }
    }

    fun setSelection(selectedIndex: Int) {
        binding.llNavigation.forEachIndexed { index, view ->
            val bottomNavigationBarItemView = view as BottomNavigationBarItemView
            with(bottomNavigationBarItemView) {
                if (id == selectedIndex) {
                    setSelectedImage()
                    setSelectedText()
                } else {
                    setUnSelectedImage()
                    setUnselectedText()
                }
            }
        }
    }

    fun updateImage(selectedIndex: Int, imageUrl: String, gender: Long) {
        binding.llNavigation.forEachIndexed { index, view ->
            val bottomNavigationBarItemView = view as BottomNavigationBarItemView
            with(bottomNavigationBarItemView) {
                if (index == selectedIndex) {
                    updateImage(imageUrl, gender)
                }
            }
        }
    }
}