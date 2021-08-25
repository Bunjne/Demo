package whiz.tss.sspark.s_spark_android.presentation.main

import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.tss.sspark.s_spark_android.data.viewModel.LoginViewModel
import whiz.sspark.library.utility.AppSettingManager
import whiz.tss.sspark.s_spark_android.databinding.ActivityMainBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.unility.retrieveDeviceID
import whiz.tss.sspark.s_spark_android.unility.saveAuthenticationInformation
import whiz.tss.sspark.s_spark_android.unility.saveUserID

class MainActivity : BaseActivity() {

    private val viewModel: LoginViewModel by viewModel()

    private val binding by lazy {
         ActivityMainBinding.inflate(layoutInflater)
    }

    private val appSettingManager by lazy {
        AppSettingManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun initView() {
        lifecycleScope.launch {
            appSettingManager.clearCounter()
        }

        binding.tvCount.setOnClickListener {
            val deviceID = retrieveDeviceID(this)
            viewModel.login("5913873", "1850", deviceID)
            lifecycleScope.launch {
                appSettingManager.incrementCounter()
            }
        }
    }

    override fun observeView() {

    }

    override fun observeData() {
        appSettingManager.exampleCounterFlow.asLiveData().observe(this) {
            binding.tvCount.text = "$it"
        }
    }

    override fun observeError() {

    }
}