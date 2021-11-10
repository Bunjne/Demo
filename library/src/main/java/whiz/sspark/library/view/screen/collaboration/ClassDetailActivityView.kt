package whiz.sspark.library.view.screen.collaboration

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import whiz.sspark.library.R
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.databinding.ViewClassDetailActivityBinding
import whiz.sspark.library.extension.show

class ClassDetailActivityView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassDetailActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(backgroundDrawable: GradientDrawable,
             title: String,
             subTitle: String,
             color: Int,
             bottomNavigationBarItems: List<BottomNavigationBarItem>,
             onNavigationItemSelected: (Int) -> Unit,
             onStudyPlanClicked: () -> Unit) {
        renderDetail(backgroundDrawable, title, subTitle)

        with(binding.bnvActivity) {
            init(context, bottomNavigationBarItems) {
                onNavigationItemSelected(it)
            }
        }

        with (binding.ivStudyPlan) {
            show(R.drawable.ic_clipboard)
            setColorFilter(color)
        }

        binding.tvStudyPlan.setTextColor(color)

        binding.cvStudyPlan.setOnClickListener {
            onStudyPlanClicked()
        }
    }

    fun initSegment(segmentTabTitles: List<String>,
                    textColorStateList: ColorStateList?,
                    segmentBackgroundDrawable: Drawable?,
                    onSegmentTabClicked: (Int) -> Unit) {
        binding.vSegment.init(
            titles = segmentTabTitles,
            onTabClicked = onSegmentTabClicked,
            textColorStateList = textColorStateList,
            backgroundDrawable = segmentBackgroundDrawable
        )
    }

    fun setSegmentVisibility(isVisible: Boolean) {
        binding.vSegment.visibility = if (isVisible) {
            VISIBLE
        } else {
            GONE
        }
    }

    fun setStudyPlanVisibility(isVisible: Boolean) {
        binding.cvStudyPlan.visibility = if (isVisible) {
            VISIBLE
        } else {
            GONE
        }
    }

    fun renderFragment(fragment: Fragment, supportFragmentManager: FragmentManager, selectedTabId: Int) {
        val fragmentList = supportFragmentManager.fragments
        val targetFragment = supportFragmentManager.findFragmentByTag(selectedTabId.toString())

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
                .add(R.id.flContainer, fragment, selectedTabId.toString())
                .commit()
        }

        binding.bnvActivity.setSelection(selectedTabId)
    }

    private fun renderDetail(backgroundDrawable: GradientDrawable,
                             title: String,
                             subTitle: String) {

        if (SSparkLibrary.isDarkModeEnabled) {
            binding.clHeader.setBackgroundColor(ContextCompat.getColor(context, R.color.viewBaseSecondaryColor))
            binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.textBasePrimaryColor))
            binding.tvSubTitle.setTextColor(ContextCompat.getColor(context, R.color.textBaseSecondaryColor))
        } else {
            binding.clHeader.background = backgroundDrawable
            binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.naturalV100))
            binding.tvSubTitle.setTextColor(ContextCompat.getColor(context, R.color.naturalV100))
        }

        binding.tvSubTitle.text = subTitle
        binding.tvTitle.text = title
    }
}