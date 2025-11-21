package com.payu.finance.di

import com.payu.finance.common.Constants
import com.payu.finance.data.api.AuthApiService
import com.payu.finance.data.api.LoanApiService
import com.payu.finance.data.api.RepaymentApiService
import com.payu.finance.data.network.CookieInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Network module for dependency injection
 * Using Koin for DI (can be replaced with Hilt/Dagger)
 */
val networkModule = module {
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Cookie interceptor to add cookies to requests (except Login and Verify OTP)
        val cookieInterceptor = CookieInterceptor(get())

        // Header interceptor to add required headers
        val headerInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("X-PLATFORM-KEY", "LoanDocPlatform")
                .header("Accept-Language", "en-GB,en;q=0.9")
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .header("Connection", "keep-alive")
            
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(cookieInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<LoanApiService> {
        get<Retrofit>().create(LoanApiService::class.java)
    }

    single<RepaymentApiService> {
        get<Retrofit>().create(RepaymentApiService::class.java)
    }

    single<AuthApiService> {
        get<Retrofit>().create(AuthApiService::class.java)
    }
    
    single<com.payu.finance.data.api.ScreenContentApiService> {
        get<Retrofit>().create(com.payu.finance.data.api.ScreenContentApiService::class.java)
    }
}

