package whiz.sspark.library.view.screen.learning_outcome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.databinding.ViewSchollRecordActivityBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showViewStateX

class SchoolRecordActivityView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewSchollRecordActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String,
             selectedTerm: String,
             segmentTitles: List<String>,
             onSelectTerm: () -> Unit,
             onSegmentClicked: (Int) -> Unit) {
        binding.ivDropdown.show(R.drawable.ic_dropdown)

        binding.tvTitle.text = title
        binding.tvTerm.text = selectedTerm
        binding.vSegment.init(segmentTitles, onSegmentClicked)

        binding.cvTerm.setOnClickListener {
            onSelectTerm()
        }
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun changeTerm(title: String,
                   selectedTerm: String) {
        binding.tvTitle.text = title
        binding.tvTerm.text = selectedTerm
    }

    fun renderFragment(supportFragmentManager: FragmentManager,
                       fragment: Fragment,
                       fragmentId: Int) {
        val fragmentList = supportFragmentManager.fragments
        val targetFragment = supportFragmentManager.findFragmentByTag(fragmentId.toString())

        fragmentList.forEach {
            supportFragmentManager.beginTransaction()
                .hide(it)
                .commit()
        }

        targetFragment?.let {
            supportFragmentManager.beginTransaction()
                .show(it)
                .commit()
        } ?: run {
            supportFragmentManager.beginTransaction()
                .add(R.id.flContainer, fragment, fragmentId.toString())
                .commit()
        }
    }

    fun forceRenderNewFragment(supportFragmentManager: FragmentManager,
                          fragment: Fragment,
                          fragmentId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flContainer, fragment, fragmentId.toString())
            .commit()
    }
}