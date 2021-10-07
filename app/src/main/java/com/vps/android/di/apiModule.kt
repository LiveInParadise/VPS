package com.vps.android.di

import com.vps.android.interactors.auth.api.AuthApi
import com.vps.android.interactors.mechanism.api.MechanismApi
import com.vps.android.interactors.task.api.TaskApi
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    single { get<Retrofit>().create(AuthApi::class.java) }
    single { get<Retrofit>().create(MechanismApi::class.java) }
    single { get<Retrofit>().create(TaskApi::class.java) }
}
