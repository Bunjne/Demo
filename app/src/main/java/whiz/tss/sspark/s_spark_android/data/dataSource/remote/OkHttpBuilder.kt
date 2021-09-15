package whiz.tss.sspark.s_spark_android.data.dataSource.remote

import android.content.Context
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import whiz.sspark.library.BuildConfig
import whiz.tss.sspark.s_spark_android.utility.retrieveAuthenticationInformation
import java.util.concurrent.TimeUnit

class OkHttpBuilder(private val context: Context) {

    private val logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    }

    fun baseLoginAPI(apiKey: String): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", apiKey)
                .build()
            chain.proceed(newRequest)
        }
        .addInterceptor(logging)
        .build()

    fun baseAPI(authenticator: Authenticator, apiKey: String): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .authenticator(authenticator)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
            val token = retrieveAuthenticationInformation(context)?.accessToken

            if (!token.isNullOrBlank()) {
//                request.addHeader("Authorization", "bearer $token")
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("x-api-key", apiKey)
                request.addHeader("Content-Type", "application/json")
            } else {
                request
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", apiKey)
            }

            chain.proceed(request.build())
        }
        .addInterceptor(logging)
        .build()
}