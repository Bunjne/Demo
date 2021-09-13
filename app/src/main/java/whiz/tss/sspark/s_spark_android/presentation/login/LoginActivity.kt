package whiz.tss.sspark.s_spark_android.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.lifecycle.lifecycleScope
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.AuthenticationInformation
import whiz.sspark.library.extension.setGradientDrawable
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.data.viewModel.LoginViewModel
import whiz.tss.sspark.s_spark_android.databinding.ActivityLoginBinding
import whiz.tss.sspark.s_spark_android.presentation.collaboration.ClassDetailActivity
import whiz.tss.sspark.s_spark_android.presentation.main.MainActivity
import whiz.tss.sspark.s_spark_android.utility.*

class LoginActivity : LocalizationActivity() {

    private val viewModel: LoginViewModel by viewModel()

    private lateinit var binding: ActivityLoginBinding

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
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        observeView()
        observeData()
        observeError()

        autoLogin()
    }

    private fun autoLogin() {
        val authenticationInformation = retrieveAuthenticationInformation(this)
        if (authenticationInformation != null) {
            verifyAuthenticationInformation(authenticationInformation)
        } else {
            //TODO remove mock data when screen confirmed
            val deviceID = retrieveDeviceID(this)
            viewModel.login("test2", "123456", deviceID, operatorName)
        }
    }

    private fun initView() {

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
                verifyAuthenticationInformation(it)
            }
        }

        viewModel.profileResponse.observe(this) {
            it?.let {
                saveUserID(this, it.userId)

                lifecycleScope.launch {
                    profileManager.saveStudent(it)
                    val intent = Intent(this@LoginActivity, ClassDetailActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }
    }

    private fun observeError() {
        viewModel.errorMessage.observe(this) {
            it?.let {
                showAlertWithOkButton(it)
            }
        }

        viewModel.loginErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.profileErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(this, it)
            }
        }
    }

    private fun verifyAuthenticationInformation(authenticationInformation: AuthenticationInformation) {
        when (authenticationInformation.role) {
            RoleType.JUNIOR.type -> {
                SSparkApp.setJuniorApp()
                viewModel.getStudentProfile()
            }
            RoleType.SENIOR.type -> {
                SSparkApp.setSeniorApp()
                viewModel.getStudentProfile()
            }
            RoleType.INSTRUCTOR.type -> {
                SSparkApp.setInstructorApp()
                //TODO wait implement instructor API
            }
            RoleType.GUARDIAN.type -> {
                SSparkApp.setGuardianApp()
                //TODO wait implement guardian API
            }
            else -> {
                showAlertWithOkButton(resources.getString(R.string.general_alertmessage_cannot_specify_role))
                clearData(this)
            }
        }
    }
}