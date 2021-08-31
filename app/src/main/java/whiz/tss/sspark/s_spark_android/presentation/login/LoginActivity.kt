package whiz.tss.sspark.s_spark_android.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.lifecycle.lifecycleScope
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Profile
import whiz.sspark.library.utility.showApiResponseAlert
import whiz.tss.sspark.s_spark_android.data.viewModel.LoginViewModel
import whiz.tss.sspark.s_spark_android.databinding.ActivityLoginBinding
import whiz.tss.sspark.s_spark_android.presentation.main.MainActivity
import whiz.tss.sspark.s_spark_android.unility.*
import java.lang.RuntimeException

class LoginActivity : LocalizationActivity() {

    private val viewModel: LoginViewModel by viewModel()

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val profileManager by lazy {
        ProfileManager(this)
    }

    private val operatorName by lazy {
        try {
            (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkOperatorName
        } catch (exception: Exception) {
            ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        observeView()
        observeData()
        observeError()
    }

    private fun initView() {
        val authenticationInformation = retrieveAuthenticationInformation(this)
        if (authenticationInformation != null) {
            viewModel.getProfile()
        } else {
            val deviceID = retrieveDeviceID(this)
            viewModel.login("6113187", "1850", deviceID, operatorName)
        }
    }

    private fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            if (isLoading) {

            } else {

            }
        }
    }

    private fun observeData() {
        viewModel.loginResponse.observe(this) {
            it?.let {
                saveAuthenticationInformation(this, it)

                viewModel.getProfile()
            }
        }

        viewModel.profileResponse.observe(this) {
            it?.let {
                saveUserID(this, it.userId)

                lifecycleScope.launch {
                    profileManager.saveStudent(it)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }
    }

    private fun observeError() {
        viewModel.loginErrorResponse.observe(this) {
            it?.let {
                showApiResponseAlert(this, it)
            }
        }

        viewModel.profileErrorResponse.observe(this) {
            it?.let {
                showApiResponseAlert(this, it)
            }
        }
    }
}