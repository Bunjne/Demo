package whiz.sspark.library.data.data_source.remote

import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import whiz.sspark.library.BuildConfig
import whiz.sspark.library.SSparkLibrary
import java.util.concurrent.TimeUnit

class LibraryOkHttpBuilder {

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
//                .addHeader("x-api-key", apiKey) //TODO uncomment when API available
                .build()
            chain.proceed(newRequest)
        }
        .addInterceptor(logging)
        .build()

    fun baseAPI(authenticator: Authenticator): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .authenticator(authenticator)
        .addInterceptor(SSparkLibrary.baseInterceptor)
        .addInterceptor(logging)
        .build()
}