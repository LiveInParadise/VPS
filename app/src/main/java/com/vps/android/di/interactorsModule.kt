package com.vps.android.di

import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.auth.repository.AuthRepository
import com.vps.android.interactors.auth.repository.AuthRepositoryImpl
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.interactors.mechanism.repository.MechanismRepository
import com.vps.android.interactors.mechanism.repository.MechanismRepositoryImpl
import com.vps.android.interactors.task.TaskInteractor
import com.vps.android.interactors.task.repository.TaskRepository
import com.vps.android.interactors.task.repository.TaskRepositoryImpl
import org.koin.dsl.module

val interactorsModule = module {

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get(), get()) }
    single { AuthInteractor(get()) }

    single<MechanismRepository> { MechanismRepositoryImpl(get(), get(), get(), get(), get()) }
    single { MechanismInteractor(get()) }

    single<TaskRepository> { TaskRepositoryImpl(get(), get(), get(), get(), get()) }
    single { TaskInteractor(get()) }

}
