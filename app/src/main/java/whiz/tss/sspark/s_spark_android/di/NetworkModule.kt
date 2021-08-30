package whiz.tss.sspark.s_spark_android.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.dataSource.remote.GsonConverterBuilder
import whiz.sspark.library.data.dataSource.remote.RetrofitBuilder
import whiz.sspark.library.data.dataSource.remote.service.ProfileService
import whiz.sspark.library.data.repository.ProfileRepositoryImpl
import whiz.tss.sspark.s_spark_android.data.dataSource.remote.service.LoginService
import whiz.tss.sspark.s_spark_android.data.dataSource.remote.OkHttpBuilder
import whiz.tss.sspark.s_spark_android.data.dataSource.remote.TokenAuthenticator
import whiz.tss.sspark.s_spark_android.data.repository.LoginRepositoryImpl
import whiz.tss.sspark.s_spark_android.data.viewModel.LoginViewModel

val networkModule = module {
    factory<LoginService> { RetrofitBuilder(OkHttpBuilder(androidContext()).baseLoginAPI(SSparkLibrary.apiKey), get()).build(SSparkLibrary.baseUrl) } //

    factory { OkHttpBuilder(androidContext()).baseAPI(TokenAuthenticator(androidContext(), get()), SSparkLibrary.apiKey) }
    factory { RetrofitBuilder(get(), get()) }

    single<Converter.Factory> { GsonConverterBuilder().build() }

    factory<ProfileService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }

    factory { LoginRepositoryImpl(androidContext(), get()) }
    factory { ProfileRepositoryImpl(androidContext(), get()) }

    viewModel { LoginViewModel(get(), get()) }
}