package com.paruyr.scopictask.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.paruyr.scopictask.data.config.ConfigStorage
import com.paruyr.scopictask.data.db.RoomDbDataSource
import com.paruyr.scopictask.data.network.AuthenticationDataSource
import com.paruyr.scopictask.data.network.FirestoreDataSource
import com.paruyr.scopictask.data.repository.ConfigRepository
import com.paruyr.scopictask.data.repository.FirestoreItemDataRepository
import com.paruyr.scopictask.data.repository.RoomItemDataRepository
import com.paruyr.scopictask.data.repository.SignInRepository
import com.paruyr.scopictask.data.repository.SignOutRepository
import com.paruyr.scopictask.data.repository.SignUpRepository
import com.paruyr.scopictask.utils.Constants.PREFERENCE_DATA_STORE_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_DATA_STORE_NAME)

val dataStoreModule = module {
    single { androidContext().dataStore }
    single { ConfigStorage(get()) }
    factory { SignInRepository(get()) }
    factory { SignUpRepository(get()) }
    factory { AuthenticationDataSource(get()) }
    factory { ConfigRepository(get()) }
    factory { SignOutRepository(get()) }
    factory { RoomItemDataRepository(get()) }
    factory { FirestoreItemDataRepository(get()) }
    factory { FirestoreDataSource(get()) }
    factory { RoomDbDataSource(get()) }
}