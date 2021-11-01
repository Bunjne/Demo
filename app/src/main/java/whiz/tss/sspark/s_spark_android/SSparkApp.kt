package whiz.tss.sspark.s_spark_android

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import st.lowlevel.storo.Storo
import st.lowlevel.storo.StoroBuilder
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.enum.ProjectType
import whiz.sspark.library.di.*
import whiz.sspark.library.di.remoteModule
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.di.networkModule
import whiz.tss.sspark.s_spark_android.extension.getAuthorizationToken
import whiz.tss.sspark.s_spark_android.extension.getRoleType
import whiz.tss.sspark.s_spark_android.utility.getAPKSignedSignature
import whiz.tss.sspark.s_spark_android.utility.logout
import whiz.tss.sspark.s_spark_android.utility.retrieveAuthenticationInformation

class SSparkApp: Application() {

    init {
        instance = this
    }

    private external fun getApiBaseURL(key: String): String
    private external fun getApiKey(key: String): String
    private external fun getClientId(key: String): String
    private external fun getClientSecret(key: String): String
    private external fun getCollaborationSocketURL(key: String): String

    companion object {
        init {
            System.loadLibrary("keys")
        }

        private var instance: SSparkApp? = null
        private var _role: RoleType? = null

        fun setStudentJuniorApp() {
            _role = RoleType.STUDENT_JUNIOR
        }

        fun setStudentSeniorApp() {
            _role = RoleType.STUDENT_SENIOR
        }

        fun setInstructorJuniorApp() {
            _role = RoleType.INSTRUCTOR_JUNIOR
        }

        fun setInstructorSeniorApp() {
            _role = RoleType.INSTRUCTOR_SENIOR
        }

        fun setGuardianApp() {
            _role = RoleType.GUARDIAN
        }

        val role get() = _role ?: retrieveAuthenticationInformation(instance!!.applicationContext)?.getRoleType() ?: throw IllegalStateException("FLAG not found")
        var scheduleTime = listOf("07:30", "08:30", "09:30", "10:30", "11:30", "12:30", "13:30", "14:30", "15:30", )
    }

    private val localizationDelegate = LocalizationApplicationDelegate()

    override fun attachBaseContext(base: Context) {
        localizationDelegate.setDefaultLanguage(base, "th")
        super.attachBaseContext(localizationDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(baseContext)
    }

    override fun onCreate() {
        super.onCreate()

        with(SSparkLibrary) {
            setProjectType(applicationContext, ProjectType.TSS)

            apiKey = getApiKey(getAPKSignedSignature(applicationContext))
            clientId = getClientId(getAPKSignedSignature(applicationContext))
            clientSecret = getClientSecret(getAPKSignedSignature(applicationContext))
            baseUrl = getApiBaseURL(getAPKSignedSignature(applicationContext))
            collaborationSocketBaseURL = getCollaborationSocketURL(getAPKSignedSignature(applicationContext))

            setOnSessionExpireCallback {
                logout(applicationContext)
            }

            setInterceptor { chain ->
                    val request = chain.request().newBuilder()
                    val token = retrieveAuthenticationInformation(this@SSparkApp)?.getAuthorizationToken()

//                    if (!token.isNullOrBlank()) { //TODO uncomment when API available
                        request.addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IkREMzlCNEM1MDRGMDhBOTY5MDdDQjUzRjM5RTg5M0QzIiwidHlwIjoiYXQrand0In0.eyJuYmYiOjE2MzM0MDY4MTgsImV4cCI6MTYzMzQxMDQxOCwiaXNzIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6NTAwMSIsImNsaWVudF9pZCI6InRzc19kZXZlbG9wbWVudCIsInN1YiI6IjgzOTI2MDNiLWI2ODMtNDA0Zi1iODI0LTJmNGRlNWRjNjY4YiIsImF1dGhfdGltZSI6MTYzMzQwNjgxOCwiaWRwIjoibG9jYWwiLCJyb2xlIjoiU1RVREVOVCIsImVudGl0eV9pZCI6IjM1ZDYyZjhhLTM2M2UtNDJmMC1hZDNiLTA4ZDk3NDQ0MmIwYyIsInVzZXJfdHlwZSI6IlNUVURFTlRfU0VOSU9SSElHSFNDSE9PTCIsImp0aSI6IkU0QTQyRjU5M0ZDMEZEMkE3RTUyMEFERTMyNzgxQjMzIiwiaWF0IjoxNjMzNDA2ODE4LCJzY29wZSI6WyJ0c3MiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsicHdkIl19.I7YRgoKtadBcnB54SG-Pv92A470RMY8rp3oY-_uqfLjT82jXHvuB-l1_8ZPD-W0Js5QC4SR9cwACKVAQtcW73tWLnsOu5dqX8AW5m5vntnpA8a_YGJygQsR772pARP0ecodIjks-4mhgKV0ffrpZUe6yktNeacrySBG9DKmPvkh16jEuKMkbSoxr-O5ddJPJkCyx7ZS7A7WfM7zDc_P5_2cfHnF-Nv40l4ZjLnkS_2YFUw96QFywXSYygR6EypjSzsGr2f67NIC90u3Gjjj9a0iilHjOzE9F2FzSmbKUGVsAVa-BMf5kMbmdpYeMPqWjNBoqllDq25G7DywO8UJoow")
//                            .addHeader("x-api-key", apiKey)
//                    } else {
//                        request.addHeader("x-api-key", apiKey)
//                    }

                    chain.proceed(request.build())
            }
        }

        if (!Storo.isInitialized()) {
            StoroBuilder.configure(Long.MAX_VALUE)
                .setDefaultCacheDirectory(this)
                .initialize()
        }

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(listOf(networkModule, remoteModule, localModule, repositoryModule, viewModelModule))
        }
    }
}