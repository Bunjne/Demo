package whiz.sspark.library.view.general.information_dialog

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewInformationDialogBinding
import whiz.sspark.library.extension.show

class InformationDialogView: FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding: ViewInformationDialogBinding by lazy {
        ViewInformationDialogBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(headerText: String,
             items: List<InformationDialogAdapter.Item>,
             onCloseClicked: () -> Unit) {
        binding.tvHeaderText.text = headerText
        binding.ivCloseButton.show(R.drawable.ic_rounded_close_button)

        with(binding.rvCalendarInformations) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = InformationDialogAdapter(
                context = context,
                items = items
            )
        }

        binding.ivCloseButton.setOnClickListener {
            onCloseClicked()
        }
    }
}