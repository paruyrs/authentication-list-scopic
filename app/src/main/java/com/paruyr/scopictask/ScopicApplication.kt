package com.paruyr.scopictask

import android.app.Application
import com.paruyr.scopictask.di.appModule
import com.paruyr.scopictask.di.dataStoreModule
import com.paruyr.scopictask.di.firebaseModule
import com.paruyr.scopictask.di.roomModule
import com.paruyr.scopictask.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ScopicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ScopicApplication)
            modules(roomModule, dataStoreModule, viewModelModule, appModule, firebaseModule)
        }
    }
}