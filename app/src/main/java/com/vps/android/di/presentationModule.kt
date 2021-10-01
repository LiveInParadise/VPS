package com.vps.android.di

import androidx.lifecycle.SavedStateHandle
import com.vps.android.presentation.auth.AuthViewModel
import com.vps.android.presentation.root.MainVm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    factory { SavedStateHandle() }

    viewModel { MainVm() }
    viewModel { AuthViewModel(get(), get()) }
}
