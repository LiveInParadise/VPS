package com.vps.android.di

import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.auth.repository.AuthRepository
import com.vps.android.interactors.auth.repository.AuthRepositoryImpl
import org.koin.dsl.module

val interactorsModule = module {

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get(), get()) }
    single { AuthInteractor(get()) }

}
