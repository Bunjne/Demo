package whiz.sspark.library.view.screen.assignment

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.databinding.ViewAssignmentActivityBinding
import whiz.sspark.library.extension.showViewStateX
import whiz.sspark.library.view.widget.assignment.AssignmentAdapter

class AssignmentActivityView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAssignmentActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init() {
        with(binding.rvAssignment) {
            layoutManager = LinearLayoutManager(context)
            adapter = AssignmentAdapter()
        }
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    suspend fun updateItem(items: PagingData<Assignment>) {
        (binding.rvAssignment.adapter as AssignmentAdapter).submitData(items)
    }
}