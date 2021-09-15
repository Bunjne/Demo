package whiz.sspark.library.view.screen.collaboration

import android.content.Context
import android.graphics.Color
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
             courseName: String,
             courseCode: String,
             color: Int,
             bottomNavigationBarItems: List<BottomNavigationBarItem>,
             onNavigationItemSelected: (Int) -> Unit,
             onStudyPlanClicked: () -> Unit) {
        renderDetail(backgroundDrawable, courseName, courseCode)

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
                             courseName: String,
                             courseCode: String) {

        if (SSparkLibrary.isDarkModeEnabled) {
            binding.clHeader.setBackgroundColor(ContextCompat.getColor(context, R.color.viewBaseSecondaryColor))
            binding.tvCourseCode.setTextColor(ContextCompat.getColor(context, R.color.textBasePrimaryColor))
            binding.tvCourseName.setTextColor(ContextCompat.getColor(context, R.color.textBaseSecondaryColor))
        } else {
            binding.clHeader.background = backgroundDrawable
            binding.tvCourseCode.setTextColor(Color.WHITE)
            binding.tvCourseName.setTextColor(Color.WHITE)
        }

        binding.tvCourseName.text = courseName
        binding.tvCourseCode.text = courseCode
    }
}