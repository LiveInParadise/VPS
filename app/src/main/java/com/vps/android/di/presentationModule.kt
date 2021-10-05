package com.vps.android.di

import androidx.lifecycle.SavedStateHandle
import com.vps.android.presentation.auth.AuthViewModel
import com.vps.android.presentation.main.MainViewModel
import com.vps.android.presentation.mechanism.MechanismViewModel
import com.vps.android.presentation.mechanism_type.MechanismTypeViewModel
import com.vps.android.presentation.root.MainVm
import com.vps.android.presentation.service.ServiceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    factory { SavedStateHandle() }

    viewModel { MainVm() }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { MechanismTypeViewModel(get(), get()) }
    viewModel { MechanismViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { ServiceViewModel(get(), get()) }
}
