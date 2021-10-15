package whiz.sspark.library.view.screen.advisory

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
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
import whiz.sspark.library.databinding.ViewAdvisoryActivityBinding

class AdvisoryActivityView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAdvisoryActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(backgroundHeaderDrawable: GradientDrawable,
             bottomNavigationBarItems: List<BottomNavigationBarItem>,
             segmentTabTitles: List<String>,
             textColorStateList: ColorStateList? = null,
             segmentBackgroundDrawable: Drawable? = null,
             onSegmentTabClicked: (Int) -> Unit,
             onNavigationItemSelected: (Int) -> Unit) {
        renderDetail(backgroundHeaderDrawable)

        binding.vSegment.init(
            titles = segmentTabTitles,
            onTabClicked = onSegmentTabClicked,
            textColorStateList = textColorStateList,
            backgroundDrawable = segmentBackgroundDrawable
        )

        with(binding.bnvActivity) {
            init(context, bottomNavigationBarItems) {
                onNavigationItemSelected(it)
            }
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

    fun setSegmentVisibility(isVisible: Boolean) {
        binding.vSegment.visibility = if (isVisible) {
            VISIBLE
        } else {
            GONE
        }
    }

    private fun renderDetail(backgroundDrawable: GradientDrawable) {
        if (SSparkLibrary.isDarkModeEnabled) {
            binding.clHeader.setBackgroundColor(ContextCompat.getColor(context, R.color.viewBaseSecondaryColor))
            binding.tvAdvisoryTitle.setTextColor(ContextCompat.getColor(context, R.color.textBasePrimaryColor))
            binding.tvSubtitle.setTextColor(ContextCompat.getColor(context, R.color.textBaseSecondaryColor))
        } else {
            binding.clHeader.background = backgroundDrawable
            binding.tvAdvisoryTitle.setTextColor(Color.WHITE)
            binding.tvSubtitle.setTextColor(Color.WHITE)
        }
    }
}