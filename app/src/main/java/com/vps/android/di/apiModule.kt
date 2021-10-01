package com.vps.android.di

import com.vps.android.interactors.auth.api.AuthApi
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    single { get<Retrofit>().create(AuthApi::class.java) }
}
