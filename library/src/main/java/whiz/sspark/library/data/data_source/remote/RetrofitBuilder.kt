package whiz.sspark.library.data.data_source.remote

import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

class RetrofitBuilder(val okHttpClient: OkHttpClient,
                      val converterFactory: Converter.Factory) {
    inline fun <reified T> build(baseUrl: String): T = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .build()
        .create(T::class.java)
}