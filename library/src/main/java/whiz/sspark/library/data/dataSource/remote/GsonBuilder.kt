package whiz.sspark.library.data.dataSource.remote

import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory

class GsonConverterBuilder {
    fun build(): GsonConverterFactory = GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create())
}