package whiz.tss.sspark.s_spark_android.presentation.event.event_registered

import android.os.Bundle
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityEventRegisteredBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class EventRegisteredActivity : BaseActivity() {

    private lateinit var binding: ActivityEventRegisteredBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventRegisteredBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun initView() {
        binding.vEventRegistered.init(
            segmentTitles = resources.getStringArray(R.array.event_registered_segment).toList(),
            title = resources.getString(R.string.event_registered_title),
            onTabClicked = {
                when (it) {

                }
            }
        )
    }
}