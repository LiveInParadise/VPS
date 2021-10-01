package com.vps.android.di

import com.vps.android.core.dispatchers.DispatchersProvider
import com.vps.android.core.dispatchers.DispatchersProviderImpl
import com.vps.android.core.local.PrefManager
import com.vps.android.core.utils.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharedModule = module {

    single { PrefManager(androidContext(), get()) }

    single { NetworkMonitor(androidContext()) }

    single<DispatchersProvider> { DispatchersProviderImpl() }
}
