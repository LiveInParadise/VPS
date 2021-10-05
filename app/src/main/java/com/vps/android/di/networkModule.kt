package com.vps.android.di

import com.google.gson.Gson
import com.vps.android.BuildConfig
import com.vps.android.core.network.errors.DefaultErrorMapper
import com.vps.android.core.network.errors.ErrorMapper
import com.vps.android.core.network.errors.NetworkErrorBus
import com.vps.android.core.network.interceptors.AuthInterceptor
import com.vps.android.core.network.interceptors.ErrorStatusInterceptor
import com.vps.android.core.network.interceptors.NetworkStatusInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val READ_TIMEOUT = 10_000L
private const val WRITE_TIMEOUT = 10_000L
const val CONNECT_TIMEOUT = 10_000L

const val CONNECT_TIMEOUT_HEADER = "CONNECTION_TIMEOUT"
const val UPLOAD_CONNECT_TIMEOUT = 30_000L

val networkModule = module {

    single { provideRetrofit(get()) }

    single { provideGson() }

    single {
        provideOkHttpClient(
            get(),
            get(),
            get(),
            get()
        )
    }

    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.HEADERS
            }
        }
    }

    single { NetworkStatusInterceptor(get()) }

    single { AuthInterceptor(get()) }

    single { ErrorStatusInterceptor() }

    single<ErrorMapper> { DefaultErrorMapper() }

    single { NetworkErrorBus() }
}

private fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor,
    networkStatusInterceptor: NetworkStatusInterceptor,
    errorStatusInterceptor: ErrorStatusInterceptor,
    authInterceptor: AuthInterceptor,
): OkHttpClient =
    OkHttpClient.Builder().apply {
        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
        addInterceptor(loggingInterceptor)
        addInterceptor(networkStatusInterceptor)
        addInterceptor(errorStatusInterceptor)
        addInterceptor(authInterceptor)
    }
        .build()

private fun provideRetrofit(
    client: OkHttpClient
): Retrofit {
    return Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .build()
}

private fun provideGson() = Gson()
