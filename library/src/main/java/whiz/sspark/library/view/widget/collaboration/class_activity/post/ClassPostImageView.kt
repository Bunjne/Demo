package whiz.sspark.library.view.widget.collaboration.class_activity.post

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.databinding.ViewClassPostImageBinding
import whiz.sspark.library.extension.show

class ClassPostImageView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassPostImageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(attachment: Attachment,
             navigateToImage: (ImageView, Attachment) -> Unit) {

        if (attachment.isLocal) {
            attachment.file?.let {
                binding.ivPost.show(it)
            }
        } else {
            binding.ivPost.show(attachment.url)
        }

        setOnClickListener {
            navigateToImage(binding.ivPost, attachment)
        }
    }

    fun clearImage() {
        binding.ivPost.show("")
    }
}