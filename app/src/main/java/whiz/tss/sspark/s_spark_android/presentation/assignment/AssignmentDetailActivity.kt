package whiz.tss.sspark.s_spark_android.presentation.assignment

import android.os.Bundle
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.extension.setLightStatusBar
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.utility.openFile
import whiz.tss.sspark.s_spark_android.databinding.ActivityAssignmentDetailBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.utility.showImage

class AssignmentDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityAssignmentDetailBinding

    private val assignment by lazy {
        intent.getStringExtra("assignment")!!.toObject<Assignment>()!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setLightStatusBar()

        initView()
    }

    override fun initView() {
        binding.vProfile.setBackGradientColor(assignment.startColor.toColor(), assignment.endColor.toColor())
        binding.vAssignment.init(
            assignment = assignment,
            onFileClicked = {
                openFile(this, it)
            },
            navigateToImage = { imageView, attachment ->
                showImage(this, imageView, attachment.url)
            }
        )
    }
}