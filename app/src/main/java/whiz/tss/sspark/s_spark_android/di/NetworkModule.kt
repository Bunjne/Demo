package whiz.tss.sspark.s_spark_android.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.datasource.remote.GsonConverterBuilder
import whiz.sspark.library.data.datasource.remote.RetrofitBuilder
import whiz.sspark.library.data.datasource.remote.service.ProfileService
import whiz.sspark.library.data.repository.ProfileRepositoryImpl
import whiz.tss.sspark.s_spark_android.data.datasSource.remote.service.LoginService
import whiz.tss.sspark.s_spark_android.data.datasSource.remote.OkHttpBuilder
import whiz.tss.sspark.s_spark_android.data.datasSource.remote.TokenAuthenticator
import whiz.tss.sspark.s_spark_android.data.repository.LoginRepositoryImpl
import whiz.tss.sspark.s_spark_android.data.viewModel.LoginViewModel

val networkModule = module(override = true) {
    factory { TokenAuthenticator(androidContext(), get()) }
    factory { OkHttpBuilder(get()).baseAPI(get()) }
    factory { RetrofitBuilder(get(), get()) }

    single<Converter.Factory> { GsonConverterBuilder().build() }

    single<LoginService> { RetrofitBuilder(OkHttpBuilder(get()).baseLoginAPI(), get()).build(SSparkLibrary.baseUrl) }
    single<ProfileService> { RetrofitBuilder(OkHttpBuilder(get()).baseLoginAPI(), get()).build(SSparkLibrary.baseUrl) }

    factory { LoginRepositoryImpl(androidContext(), get()) }
    factory { ProfileRepositoryImpl(androidContext(), get()) }

    viewModel { LoginViewModel(get(), get()) }
}