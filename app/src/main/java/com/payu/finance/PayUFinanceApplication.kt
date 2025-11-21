package com.payu.finance

import android.app.Application
import com.payu.finance.di.dataModule
import com.payu.finance.di.domainModule
import com.payu.finance.di.networkModule
import com.payu.finance.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Application class for dependency injection setup
 */
class PayUFinanceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@PayUFinanceApplication)
            modules(
                networkModule,
                dataModule,
                domainModule,
                viewModelModule
            )
        }
    }
}

