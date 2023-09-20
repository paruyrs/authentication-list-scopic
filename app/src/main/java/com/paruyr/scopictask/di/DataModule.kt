package com.paruyr.scopictask.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.paruyr.scopictask.data.config.ConfigStorage
import com.paruyr.scopictask.data.config.ConfigStorageImpl
import com.paruyr.scopictask.data.db.RoomDbDataSource
import com.paruyr.scopictask.data.db.RoomDbDataSourceImpl
import com.paruyr.scopictask.data.network.AuthenticationDataSource
import com.paruyr.scopictask.data.network.AuthenticationDataSourceImpl
import com.paruyr.scopictask.data.network.FirestoreDataSource
import com.paruyr.scopictask.data.network.FirestoreDataSourceImpl
import com.paruyr.scopictask.data.repository.ConfigRepository
import com.paruyr.scopictask.data.repository.ConfigRepositoryImpl
import com.paruyr.scopictask.data.repository.FirestoreItemDataRepository
import com.paruyr.scopictask.data.repository.FirestoreItemDataRepositoryImpl
import com.paruyr.scopictask.data.repository.RoomItemDataRepository
import com.paruyr.scopictask.data.repository.RoomItemDataRepositoryImpl
import com.paruyr.scopictask.data.repository.SignInRepository
import com.paruyr.scopictask.data.repository.SignInRepositoryImpl
import com.paruyr.scopictask.data.repository.SignOutRepository
import com.paruyr.scopictask.data.repository.SignOutRepositoryImpl
import com.paruyr.scopictask.data.repository.SignUpRepository
import com.paruyr.scopictask.data.repository.SignUpRepositoryImpl
import com.paruyr.scopictask.utils.Constants.PREFERENCE_DATA_STORE_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_DATA_STORE_NAME)

val dataStoreModule = module {
    single { androidContext().dataStore }
    single<ConfigStorage> { ConfigStorageImpl(get()) }
    factory<SignInRepository> { SignInRepositoryImpl(get()) }
    factory<SignUpRepository> { SignUpRepositoryImpl(get()) }
    factory<AuthenticationDataSource> { AuthenticationDataSourceImpl(get()) }
    factory<ConfigRepository> { ConfigRepositoryImpl(get()) }
    factory<SignOutRepository> { SignOutRepositoryImpl(get()) }
    factory<RoomItemDataRepository> { RoomItemDataRepositoryImpl(get()) }
    factory<FirestoreItemDataRepository> { FirestoreItemDataRepositoryImpl(get()) }
    factory<FirestoreDataSource> { FirestoreDataSourceImpl(get()) }
    factory<RoomDbDataSource> { RoomDbDataSourceImpl(get()) }
}