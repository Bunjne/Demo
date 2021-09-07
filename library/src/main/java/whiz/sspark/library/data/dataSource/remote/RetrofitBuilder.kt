package whiz.sspark.library.data.dataSource.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

class RetrofitBuilder(
    val okHttpClient: OkHttpClient,
    val converterFactory: Converter.Factory
) {
    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

    inline fun <reified T> build(baseUrl: String): T = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .build()
        .create(T::class.java)
}