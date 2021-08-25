package whiz.sspark.library.data.datasource.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GsonConverterBuilder {
    fun build(): GsonConverterFactory = GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create())
}