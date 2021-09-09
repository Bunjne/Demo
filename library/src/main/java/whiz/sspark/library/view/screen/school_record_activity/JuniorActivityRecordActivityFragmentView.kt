package whiz.sspark.library.view.screen.school_record_activity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.databinding.ViewSchoolRecordActivityBinding

class JuniorActivityRecordActivityFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewSchoolRecordActivityBinding.inflate(LayoutInflater.from(context), this, false)
    }

    fun init() {
        
    }
}