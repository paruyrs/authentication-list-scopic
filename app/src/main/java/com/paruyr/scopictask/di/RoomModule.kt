package com.paruyr.scopictask.di

import androidx.room.Room
import com.paruyr.scopictask.data.db.UserItemDatabase
import com.paruyr.scopictask.utils.Constants.DATABASE_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            UserItemDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
    single { get<UserItemDatabase>().userItemDao() }
}
