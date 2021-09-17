package whiz.sspark.library.view.screen.collaboration.class_group

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import whiz.sspark.library.data.entity.ClassGroupCourse
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewClassGroupFragmentBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.show
import whiz.sspark.library.utility.updateItem
import whiz.sspark.library.view.widget.collaboration.class_group.ClassGroupAdapter
import java.util.*

class ClassGroupFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassGroupFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(items: MutableList<ClassGroupAdapter.Item>,
             todayDate: Date,
             classLevel: String,
             schoolLogoUrl: String,
             onClassGroupItemClicked: (ClassGroupCourse) -> Unit,
             onNavigationBarItemClicked: (Int) -> Unit,
             onRefresh: () -> Unit) {

        binding.tvTodayDate.text = todayDate.convertToDateString(
            defaultPattern = DateTimePattern.todayAbbreviatedDateFormatEn,
            dayMonthThPattern = DateTimePattern.classGroupDayMonthFormatTh,
            yearThPattern = DateTimePattern.generalYear
        )

        binding.tvClassLevel.text = classLevel
        binding.ivSchoolLogo.show(schoolLogoUrl)

        binding.srlClassGroup.setOnRefreshListener {
            clearData(items)
            onRefresh()
        }

        with(binding.rvClassGroup) {
            val gridLayoutManager = GridLayoutManager(context, 2)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when(adapter?.getItemViewType(position)) {
                        ClassGroupAdapter.ClassGroupItemType.NAVIGATION_BAR.type -> 2
                        ClassGroupAdapter.ClassGroupItemType.HEADER_BAR.type -> 2
                        else -> 1
                    }
                }
            }
            layoutManager = gridLayoutManager
            adapter = ClassGroupAdapter(
                context = context,
                items = items,
                onNavigationBarItemClicked = onNavigationBarItemClicked,
                onClassGroupItemClicked = onClassGroupItemClicked
            )
        }
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlClassGroup.isRefreshing = isLoading == true
    }

    fun renderData(items: MutableList<ClassGroupAdapter.Item>, newItems: MutableList<ClassGroupAdapter.Item>) {
        newItems.add(0, items.first())

        binding.rvClassGroup.adapter?.updateItem(items, newItems)

        if (newItems.isEmpty()) {
            setNoClassGroupVisibility(true)
        } else {
            setNoClassGroupVisibility(false)
        }
    }

    fun setNoClassGroupVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.tvNoClassGroup.visibility = View.VISIBLE
        } else {
            binding.tvNoClassGroup.visibility = View.INVISIBLE
        }
    }

    private fun clearData(items: MutableList<ClassGroupAdapter.Item>) {
        items.removeAll { it.navigationBarItem == null }

        binding.rvClassGroup.adapter?.notifyDataSetChanged()
    }
}