package whiz.sspark.library.view.screen.homeroom

import android.content.Context
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
import whiz.sspark.library.databinding.ViewHomeroomActivityBinding

class HomeroomActivityView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewHomeroomActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(backgroundDrawable: GradientDrawable,
             level: String,
             room: String,
             bottomNavigationBarItems: List<BottomNavigationBarItem>,
             onNavigationItemSelected: (Int) -> Unit) {
        renderDetail(backgroundDrawable, level, room)

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

    private fun renderDetail(backgroundDrawable: GradientDrawable,
                             level: String,
                             room: String) {

        if (SSparkLibrary.isDarkModeEnabled) {
            binding.clHeader.setBackgroundColor(ContextCompat.getColor(context, R.color.viewBaseSecondaryColor))
            binding.tvHomeroomTitle.setTextColor(ContextCompat.getColor(context, R.color.textBasePrimaryColor))
            binding.tvHomeroomLevel.setTextColor(ContextCompat.getColor(context, R.color.textBaseSecondaryColor))
        } else {
            binding.clHeader.background = backgroundDrawable
            binding.tvHomeroomTitle.setTextColor(ContextCompat.getColor(context, R.color.naturalV100))
            binding.tvHomeroomLevel.setTextColor(ContextCompat.getColor(context, R.color.naturalV100))
        }

        binding.tvHomeroomLevel.text = resources.getString(R.string.class_group_junior_class_level_place_holder, level, room)
    }
}