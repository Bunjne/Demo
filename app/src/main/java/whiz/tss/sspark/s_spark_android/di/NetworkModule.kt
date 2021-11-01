package whiz.tss.sspark.s_spark_android.di

import okhttp3.Authenticator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.data_source.remote.GsonConverterBuilder
import whiz.sspark.library.data.data_source.remote.LibraryOkHttpBuilder
import whiz.sspark.library.data.data_source.remote.RetrofitBuilder
import whiz.tss.sspark.s_spark_android.data.dataSource.remote.TokenAuthenticator
import whiz.tss.sspark.s_spark_android.data.dataSource.remote.service.LoginService
import whiz.tss.sspark.s_spark_android.data.repository.LoginRepositoryImpl
import whiz.tss.sspark.s_spark_android.data.viewModel.LoginViewModel

val networkModule = module {
    single<Converter.Factory> { GsonConverterBuilder().build() }
    single<Authenticator> { TokenAuthenticator(androidContext(), get()) }
    single { LibraryOkHttpBuilder().baseAPI(get()) }

    factory<LoginService> { RetrofitBuilder(LibraryOkHttpBuilder().baseLoginAPI(SSparkLibrary.apiKey), get()).build(SSparkLibrary.baseUrl) }

    factory { LoginRepositoryImpl(androidContext(), get()) }
    viewModel { LoginViewModel(get(), get()) }
}