package com.paruyr.scopictask.di

import com.paruyr.scopictask.ui.home.list.InputViewModel
import com.paruyr.scopictask.viewmodel.HomeViewModel
import com.paruyr.scopictask.viewmodel.LandingViewModel
import com.paruyr.scopictask.viewmodel.ListViewModel
import com.paruyr.scopictask.viewmodel.ProfileViewModel
import com.paruyr.scopictask.viewmodel.SignInViewModel
import com.paruyr.scopictask.viewmodel.SignUpViewModel
import com.paruyr.scopictask.viewmodel.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LandingViewModel(get()) }
    viewModel { SignInViewModel(get(), get(), get()) }
    viewModel { SignUpViewModel(get(), get(), get()) }
    viewModel { WelcomeViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ListViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { InputViewModel() }
}