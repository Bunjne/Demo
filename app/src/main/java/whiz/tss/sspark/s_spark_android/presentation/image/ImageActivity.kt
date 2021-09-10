package whiz.tss.sspark.s_spark_android.presentation.image

import android.os.Bundle
import whiz.sspark.library.extension.show
import whiz.tss.sspark.s_spark_android.databinding.ActivityImageBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class ImageActivity : BaseActivity() {

    private val imageUrl by lazy {
        intent?.getStringExtra("image") ?: ""
    }

    private lateinit var binding: ActivityImageBinding

    private val minimum = 0.5f
    private val medium = 0.8f
    private val maximum = 5.75f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pvImage.apply {
            minimumScale = minimum
            mediumScale = medium
            maximumScale = maximum
            show(imageUrl)
            scale = medium
        }

        binding.ivClose.setOnClickListener {
            onBackPressed()
        }
    }
}
