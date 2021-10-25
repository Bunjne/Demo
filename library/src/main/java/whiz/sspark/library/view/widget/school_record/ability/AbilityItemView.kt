package whiz.sspark.library.view.widget.school_record.ability

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.Ability
import whiz.sspark.library.databinding.ViewAbilityItemBinding

class AbilityItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAbilityItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(ability: Ability) {
        with(ability) {
            binding.tvTitle.text = title
            binding.vProgressBar.init(
                currentProgress = value,
                titles = indicators
            )
        }
    }
}