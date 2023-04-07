package com.petrs.smartlab.di

import com.petrs.smartlab.data.api.SmartLabApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://medic.madskill.ru"
private const val RETROFIT = "RETROFIT"

val networkModule = module {
    single { provideLoggingInterceptor() }
    single { provideOkHttpClient(loggingInterceptor = get()) }
    single(named(RETROFIT)) { provideRetrofit(client = get()) }
    single { provideSmartLabApi(get(named(RETROFIT))) }
}

private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(interceptor = loggingInterceptor)
        .writeTimeout(timeout = 5, unit = TimeUnit.MINUTES)
        .readTimeout(timeout = 5, unit = TimeUnit.MINUTES)
        .build()

fun provideRetrofit(client: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

fun provideSmartLabApi(retrofit: Retrofit): SmartLabApi =
    retrofit.create(SmartLabApi::class.java)