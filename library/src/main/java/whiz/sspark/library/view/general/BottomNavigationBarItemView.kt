package whiz.sspark.library.view.general

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.data.enum.BottomNavigationType
import whiz.sspark.library.databinding.ViewBottomNavigationBarItemBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle

class BottomNavigationBarItemView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var mainColor: Int = 0
    private var isHasTint = true

    private val binding by lazy {
        ViewBottomNavigationBarItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(context: Context,
             item: BottomNavigationBarItem,
             onSelected: (Int) -> Unit) {

        mainColor = ContextCompat.getColor(context, R.color.primaryColor)

        with(item) {
            when (type) {
                BottomNavigationType.PROFILE.id -> {
                    binding.ivIcon.visibility = View.INVISIBLE
                    isHasTint = false
                    binding.cvImageUrl.setCardBackgroundColor(ContextCompat.getColor(context, R.color.textBaseThirdColor))

                    with(binding.ivIconUrl) {
                        showUserProfileCircle(imageUrl, gender)
                    }
                }
                BottomNavigationType.BANK.id -> {
                    isHasTint = false
                    imageResource?.let {
                        binding.cvImageUrl.visibility = View.INVISIBLE
                        binding.ivIcon.show(it)
                    } ?: run {
                        binding.ivIcon.visibility = INVISIBLE
                        binding.ivIconUrl.show(imageUrl)
                    }
                }
                BottomNavigationType.ICON.id -> {
                    isHasTint = true
                    imageResource?.let {
                        binding.cvImageUrl.visibility = View.INVISIBLE
                        binding.ivIcon.show(it)
                    }
                }
                else -> {
                    isHasTint = true
                    imageResource?.let {
                        binding.cvImageUrl.visibility = View.INVISIBLE
                        binding.ivIcon.show(it)
                    }

                    val bottomMenuStates = arrayOf(
                        intArrayOf(android.R.attr.state_enabled),
                        intArrayOf(-android.R.attr.state_enabled)
                    )

                    val colorState = ColorStateList(bottomMenuStates, colors.toIntArray())

                    binding.tvTitle.setTextColor(colorState)
                    binding.ivIcon.imageTintList = colorState

                    if (colors.isNotEmpty()) {
                        mainColor = colors.first()
                    }
                }
            }
            binding.tvTitle.text = title
        }

        setOnClickListener {
            onSelected(item.id)
        }
    }

    fun setSelectedImage() {
        if (isHasTint) {
            binding.ivIcon.setColorFilter(mainColor)
        } else {
            binding.cvImageUrl.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primaryColor))
        }
    }

    fun setUnSelectedImage() {
        if (isHasTint) {
            binding.ivIcon.setColorFilter(R.color.textBaseThirdColor)
        } else {
            binding.cvImageUrl.setCardBackgroundColor(ContextCompat.getColor(context, R.color.textBaseThirdColor))
        }
    }

    fun setSelectedText() {
        binding.tvTitle.setTextColor(mainColor)
    }

    fun setUnselectedText() {
        binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.textBaseThirdColor))
    }

    fun updateImage(imageUrl: String, gender: Long) {
        binding.ivIconUrl.showUserProfileCircle(imageUrl, gender)
    }
}