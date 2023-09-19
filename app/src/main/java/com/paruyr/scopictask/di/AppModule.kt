package com.paruyr.scopictask.di

import com.paruyr.scopictask.utils.Validation
import org.koin.dsl.module

val appModule = module {
    single { Validation() }
}