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
import whiz.sspark.library.di.localModule
import whiz.sspark.library.di.remoteModule
import whiz.sspark.library.di.repositoryModule
import whiz.sspark.library.di.viewModelModule
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.di.networkModule
import whiz.tss.sspark.s_spark_android.extension.getRoleType
import whiz.tss.sspark.s_spark_android.utility.getAPKSignedSignature
import whiz.tss.sspark.s_spark_android.utility.logout
import whiz.tss.sspark.s_spark_android.utility.retrieveAuthenticationInformation
import java.util.*

class SSparkApp: Application() {

    init {
        instance = this
    }

    private external fun getApiBaseURL(key: String): String
    private external fun getApiKey(key: String): String
    private external fun getApiBaseURLV3(key: String): String

    companion object {
        init {
            System.loadLibrary("keys")
        }

        private var instance: SSparkApp? = null

        var collaborationSocketBaseURL: String? = null

        private var _role: RoleType? = null
        val role get() = _role ?: retrieveAuthenticationInformation(instance!!.applicationContext)?.getRoleType() ?: throw IllegalStateException("FLAG not found")

        fun setJuniorApp() {
            _role = RoleType.JUNIOR
        }

        fun setSeniorApp() {
            _role = RoleType.SENIOR
        }

        fun setInstructorApp() {
            _role = RoleType.INSTRUCTOR
        }

        fun setGuardianApp() {
            _role = RoleType.GUARDIAN
        }
    }

    private val localizationDelegate = LocalizationApplicationDelegate()

    override fun attachBaseContext(base: Context) {
        localizationDelegate.setDefaultLanguage(base, Locale.ENGLISH)
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
            baseUrl = getApiBaseURL(getAPKSignedSignature(applicationContext))
            baseUrlV3 = getApiBaseURLV3(getAPKSignedSignature(applicationContext))
            setOnSessionExpireCallback {
                logout(applicationContext)
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