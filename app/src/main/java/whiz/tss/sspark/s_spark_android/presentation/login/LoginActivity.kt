package whiz.tss.sspark.s_spark_android.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.AuthenticationInformation
import whiz.sspark.library.extension.setGradientDrawable
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.general.loading_dialog.SSparkLoadingDialog
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.data.viewModel.LoginViewModel
import whiz.tss.sspark.s_spark_android.databinding.ActivityLoginBinding
import whiz.tss.sspark.s_spark_android.extension.getRoleType
import whiz.tss.sspark.s_spark_android.presentation.collaboration.ClassDetailActivity
import whiz.tss.sspark.s_spark_android.presentation.main.MainActivity
import whiz.tss.sspark.s_spark_android.utility.*

class LoginActivity : LocalizationActivity() {

    private val viewModel: LoginViewModel by viewModel()

    private lateinit var binding: ActivityLoginBinding

    private val profileManager by lazy {
        ProfileManager(this)
    }

    private val loadingDialog by lazy {
        SSparkLoadingDialog(this)
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
//            viewModel.login("sjunior", "{{password}}")
            viewModel.login("ssenior", "{{password}}")
//            viewModel.login("iJunior", "{{password}}")
//            viewModel.login("iSenior", "{{password}}")
        }
    }

    private fun initView() {

    }

    private fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            if (isLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
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

        viewModel.studentProfileResponse.observe(this) {
            it?.let {
                saveUserID(this, it.id)

                lifecycleScope.launch {
                    profileManager.saveStudent(it)
                    val intent = Intent(this@LoginActivity, ClassDetailActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }

        viewModel.instructorProfileResponse.observe(this) {
            it?.let {
                saveUserID(this, it.id)

                lifecycleScope.launch {
                    profileManager.saveInstructor(it)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
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

        viewModel.studentProfileErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.instructorProfileErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(this, it)
            }
        }
    }

    private fun verifyAuthenticationInformation(authenticationInformation: AuthenticationInformation) {
        when (authenticationInformation.getRoleType()) {
            RoleType.STUDENT_JUNIOR -> {
                SSparkApp.setStudentJuniorApp()
                viewModel.getStudentProfile()
            }
            RoleType.STUDENT_SENIOR -> {
                SSparkApp.setStudentSeniorApp()
                viewModel.getStudentProfile()
            }
            RoleType.INSTRUCTOR_JUNIOR -> {
                SSparkApp.setInstructorJuniorApp()
                viewModel.getInstructorProfile()
            }
            RoleType.INSTRUCTOR_SENIOR -> {
                SSparkApp.setInstructorSeniorApp()
                viewModel.getInstructorProfile()
            }
            RoleType.GUARDIAN -> {
                SSparkApp.setGuardianApp()
                //TODO wait implement guardian API
            }
        }
    }
}