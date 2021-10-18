package whiz.sspark.library.view.screen.collaboration.course_syllabus

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewCourseSyllabusFragmentBinding

class CourseSyllabusFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewCourseSyllabusFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(segmentTitles: List<String>,
             startColor: Int,
             endColor: Int,
             onTabClicked: (Int) -> Unit,
             onCloseClicked: () -> Unit) {
        with(binding.cvHeader) {
            background_Gradient_Colors = intArrayOf(startColor, endColor)
            invalidate()
        }

        binding.vSegment.init(
            titles = segmentTitles,
            onTabClicked = onTabClicked
        )

        binding.tvClose.setOnClickListener {
            onCloseClicked()
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
    }
}