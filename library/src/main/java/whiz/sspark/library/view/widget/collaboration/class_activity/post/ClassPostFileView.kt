package whiz.sspark.library.view.widget.collaboration.class_activity.post

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.databinding.ViewClassPostFileBinding
import whiz.sspark.library.extension.show

class ClassPostFileView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassPostFileBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(attachment: Attachment, onFileClicked: (Attachment) -> Unit) {
        binding.ivFile.show(R.drawable.ic_post_file)
        with(attachment) {
            binding.tvFileName.text = name
            binding.tvFileType.text = extension.substring(1)

            setOnClickListener {
                onFileClicked(attachment)
            }
        }
    }
}