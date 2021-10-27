package whiz.tss.sspark.s_spark_android.data.dataSource.remote

import android.content.Context
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import whiz.sspark.library.BuildConfig
import whiz.tss.sspark.s_spark_android.extension.getAuthorizationToken
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
//                .addHeader("x-api-key", apiKey) //TODO uncomment when API available
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
            val token = retrieveAuthenticationInformation(context)?.getAuthorizationToken()

            if (!token.isNullOrBlank()) {
                request.addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IkREMzlCNEM1MDRGMDhBOTY5MDdDQjUzRjM5RTg5M0QzIiwidHlwIjoiYXQrand0In0.eyJuYmYiOjE2MzM0MDY4MTgsImV4cCI6MTYzMzQxMDQxOCwiaXNzIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6NTAwMSIsImNsaWVudF9pZCI6InRzc19kZXZlbG9wbWVudCIsInN1YiI6IjgzOTI2MDNiLWI2ODMtNDA0Zi1iODI0LTJmNGRlNWRjNjY4YiIsImF1dGhfdGltZSI6MTYzMzQwNjgxOCwiaWRwIjoibG9jYWwiLCJyb2xlIjoiU1RVREVOVCIsImVudGl0eV9pZCI6IjM1ZDYyZjhhLTM2M2UtNDJmMC1hZDNiLTA4ZDk3NDQ0MmIwYyIsInVzZXJfdHlwZSI6IlNUVURFTlRfU0VOSU9SSElHSFNDSE9PTCIsImp0aSI6IkU0QTQyRjU5M0ZDMEZEMkE3RTUyMEFERTMyNzgxQjMzIiwiaWF0IjoxNjMzNDA2ODE4LCJzY29wZSI6WyJ0c3MiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsicHdkIl19.I7YRgoKtadBcnB54SG-Pv92A470RMY8rp3oY-_uqfLjT82jXHvuB-l1_8ZPD-W0Js5QC4SR9cwACKVAQtcW73tWLnsOu5dqX8AW5m5vntnpA8a_YGJygQsR772pARP0ecodIjks-4mhgKV0ffrpZUe6yktNeacrySBG9DKmPvkh16jEuKMkbSoxr-O5ddJPJkCyx7ZS7A7WfM7zDc_P5_2cfHnF-Nv40l4ZjLnkS_2YFUw96QFywXSYygR6EypjSzsGr2f67NIC90u3Gjjj9a0iilHjOzE9F2FzSmbKUGVsAVa-BMf5kMbmdpYeMPqWjNBoqllDq25G7DywO8UJoow") //TODO uncomment when API available
//                    .addHeader("x-api-key", apiKey) //TODO uncomment when API available
            } else {
//                request.addHeader("x-api-key", apiKey) //TODO uncomment when API available
            }

            chain.proceed(request.build())
        }
        .addInterceptor(logging)
        .build()
}