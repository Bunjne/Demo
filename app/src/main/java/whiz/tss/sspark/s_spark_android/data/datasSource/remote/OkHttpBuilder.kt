package whiz.tss.sspark.s_spark_android.data.datasSource.remote

import android.content.Context
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import whiz.sspark.library.BuildConfig
import whiz.tss.sspark.s_spark_android.data.static.ConstantValue
import java.util.concurrent.TimeUnit

class OkHttpBuilder(private val context: Context) {
    private val token = context.getSharedPreferences(ConstantValue.sharedPreferencesAuthenticationInformationKey, Context.MODE_PRIVATE).getString("token", "")

    private val logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    }

    fun baseLoginAPI(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", "vZ8F8Nc4GUXG3zD8f3um7WpbuaKBjU")
                .build()
            chain.proceed(newRequest)
        }
        .addInterceptor(logging)
        .build()

    fun baseAPI(authenticator: Authenticator): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .authenticator(authenticator)
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", "vZ8F8Nc4GUXG3zD8f3um7WpbuaKBjU")
                .build()
            chain.proceed(newRequest)
        }
        .addInterceptor(logging)
        .build()
}