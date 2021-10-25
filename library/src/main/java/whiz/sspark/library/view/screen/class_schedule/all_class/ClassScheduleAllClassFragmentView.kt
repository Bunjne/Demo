package whiz.sspark.library.view.screen.class_schedule.all_class

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassScheduleAllClass
import whiz.sspark.library.databinding.ViewClassScheduleAllClassFragmentBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.view.widget.class_schedule.all_class.ClassScheduleAllClassAdapter

class ClassScheduleAllClassFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassScheduleAllClassFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(term: String,
             onCloseClicked: () -> Unit,
             onRefresh: () -> Unit) {
        with(binding.ivClose) {
            show(R.drawable.ic_close_bottom_sheet_dialog)
            setOnClickListener {
                onCloseClicked()
            }
        }

        with(binding.rvAllClass) {
            layoutManager = LinearLayoutManager(context)
            adapter = ClassScheduleAllClassAdapter()
        }

        binding.tvTerm.text = term
        binding.srlContainer.setOnRefreshListener(onRefresh)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun updateItem(classes: List<ClassScheduleAllClass>) {
        (binding.rvAllClass.adapter as ClassScheduleAllClassAdapter).submitList(classes)

        if (classes.isEmpty()) {
            binding.tvNoClass.visibility = View.VISIBLE
            binding.rvAllClass.visibility = View.INVISIBLE
        } else {
            binding.tvNoClass.visibility = View.INVISIBLE
            binding.rvAllClass.visibility = View.VISIBLE
        }
    }
}