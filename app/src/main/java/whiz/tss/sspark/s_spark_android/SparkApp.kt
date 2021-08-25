package whiz.tss.sspark.s_spark_android

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.enum.ProjectType
import whiz.sspark.library.di.remoteModule
import whiz.tss.sspark.s_spark_android.di.networkModule
import java.util.*

class SparkApp: Application() {

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
            setProjectType(this@SparkApp, ProjectType.TSS)
        }

        startKoin {
            androidLogger()
            androidContext(this@SparkApp)
            modules(listOf(networkModule, remoteModule))
        }
    }
}