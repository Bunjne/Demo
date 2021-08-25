package whiz.tss.sspark.s_spark_android.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.tss.sspark.s_spark_android.data.viewModel.LoginViewModel
import whiz.tss.sspark.s_spark_android.databinding.ActivityLoginBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.main.MainActivity
import whiz.tss.sspark.s_spark_android.unility.retrieveAuthenticationInformation
import whiz.tss.sspark.s_spark_android.unility.saveAuthenticationInformation
import whiz.tss.sspark.s_spark_android.unility.saveUserID

class LoginActivity : LocalizationActivity() {

    private val viewModel: LoginViewModel by viewModel()

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
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
            viewModel.getProfile(authenticationInformation.accessToken)
        } else {

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

                viewModel.getProfile(it.accessToken)
            }
        }

        viewModel.profileResponse.observe(this) {
            it?.let {
                saveUserID(this, it.userId)

                val intent = Intent(this, MainActivity::class.java)
            }
        }
    }

    private fun observeError() {
        viewModel.loginErrorResponse.observe(this) {
            it?.let {
                println(it.toString())
            }
        }

        viewModel.profileErrorResponse.observe(this) {
            it?.let {
                println(it.toString())
            }
        }
    }
}