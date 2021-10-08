package whiz.sspark.library.view.screen.school_record

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Advisee
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.databinding.ViewSchoolRecordActivityBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showViewStateX

class SchoolRecordActivityView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewSchoolRecordActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String,
             term: String,
             segmentTitles: List<String>,
             onSelectTerm: (View) -> Unit,
             onSegmentClicked: (Int) -> Unit) {
        binding.ivDropdown.show(R.drawable.ic_dropdown)

        binding.tvTitle.text = title
        binding.tvTerm.text = term
        binding.vSegment.init(segmentTitles, onSegmentClicked)

        binding.cvTerm.setOnClickListener {
            if (binding.ivDropdown.visibility == View.VISIBLE) {
                onSelectTerm(binding.cvTerm)
            }
        }
    }

    fun showAdviseeProfile(advisee: Advisee) {
        binding.vAdviseeProfile.visibility = View.VISIBLE
        binding.vAdviseeProfile.init(advisee = advisee)
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun changeTerm(title: String,
                   selectedTerm: String,
                   segmentTitles: List<String>,
                   currentSegment: Int) {
        binding.tvTitle.text = title
        binding.tvTerm.text = selectedTerm
        binding.vSegment.updateSegmentTitle(segmentTitles, currentSegment)
    }

    fun setIsTermSelectable(isTermSelectable: Boolean) {
        if (isTermSelectable) {
            binding.ivDropdown.visibility = View.VISIBLE
        } else {
            binding.ivDropdown.visibility = View.GONE
        }
    }

    fun setSelectedTab(tab: Int) {
        binding.vSegment.selectTab(tab)
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