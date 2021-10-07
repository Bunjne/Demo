package whiz.sspark.library.data.data_source.remote

import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory
import whiz.sspark.library.extension.NullStringToEmptyAdapterFactory

class GsonConverterBuilder {
    fun build(): GsonConverterFactory = GsonConverterFactory.create(GsonBuilder()
        .setLenient()
        .registerTypeAdapterFactory(NullStringToEmptyAdapterFactory())
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create())
}