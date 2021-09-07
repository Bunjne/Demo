package whiz.sspark.library.view.widget.collaboration.class_activity.post

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_class_post_image.view.*
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.databinding.ViewClassPostImageBinding

class ClassPostImageView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassPostImageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(attachment: Attachment,
             navigateToImage: (ImageView, Attachment) -> Unit) {

        binding.ivPost.showAttachmentImageByURL(attachment)

        setOnClickListener {
            navigateToImage(ivPost, attachment)
        }
    }
}