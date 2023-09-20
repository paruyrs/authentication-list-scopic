package com.paruyr.scopictask.di

import com.paruyr.scopictask.utils.Validation
import com.paruyr.scopictask.utils.ValidationImpl
import org.koin.dsl.module

val appModule = module {
    single<Validation> { ValidationImpl() }
}