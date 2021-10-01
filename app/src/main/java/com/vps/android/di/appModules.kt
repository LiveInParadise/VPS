package com.vps.android.di

import org.koin.core.module.Module

val appModules: List<Module> = listOf(
    apiModule,
    sharedModule,
    presentationModule,
    interactorsModule,
    networkModule,
)
